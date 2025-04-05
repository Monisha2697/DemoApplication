package com.example.simpledemoapplication

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpledemoapplication.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefManager = SharedPrefManager(requireContext())

        postAdapter = PostAdapter { post -> val added = viewModel.toggleFavorite(post)
            if (!added) {
                Toast.makeText(requireContext(),
                    getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        if (isNetworkAvailable(requireContext())) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.posts.collect { posts ->
                        sharedPrefManager.savePosts(posts) // Save to prefs
                        postAdapter.submitList(posts)
                    }
                }
            }
        } else {
            val offlinePosts = sharedPrefManager.getPosts()
            postAdapter.submitList(offlinePosts)
            Toast.makeText(requireContext(), "No internet. Showing offline data.", Toast.LENGTH_SHORT).show()
        }



      /*  viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collect { posts ->
                    postAdapter.submitList(posts)
                }
            }
        }*/
    }

     fun isNetworkAvailable(requireContext: Context): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}