package com.example.simpledemoapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpledemoapplication.databinding.FragmentFavBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private val viewModel: PostViewModel by viewModels({ requireActivity() })
    private lateinit var adapter: FavoritePostAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoritePostAdapter(requireContext()) { post ->
            viewModel.toggleFavorite(post) // this removes from favorites
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavFragment.adapter
        }


        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    vh: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.remove_from_favorites))
                        .setMessage(getString(R.string.ask_remove_from_favorites))
                        .setPositiveButton("Yes") { _, _ ->
                            val position = viewHolder.adapterPosition
                            val posts = adapter.currentList[position]
                            viewModel.toggleFavorite(posts)
                        }
                        .setNegativeButton("No") { _, _ ->
                            adapter.notifyItemChanged(viewHolder.adapterPosition) // Reset swipe if canceled
                        }
                        .setCancelable(false)
                        .show()
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        if (isNetworkAvailable(requireContext())) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.favorites.collectLatest { favorites ->
                        adapter.submitList(favorites.toList())

                    }
                }
            }
        } else {
            val sharedPrefManager = SharedPrefManager(requireContext())
            val allPosts = sharedPrefManager.getPosts()
            val favoriteIds = sharedPrefManager.getFavorites()
            val favoritePosts = allPosts.filter { favoriteIds.contains(it.id) }
            adapter.submitList(favoritePosts)
        }

    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()
        viewModel.refreshFavorites()

    }

    private fun isNetworkAvailable(requireContext: Context): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}