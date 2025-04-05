package com.example.simpledemoapplication

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpledemoapplication.databinding.ItemFavoritePostBinding

class FavoritePostAdapter (private val context: Context ,private val onDeleteClick: (Post) -> Unit ) : ListAdapter<Post,
        FavoritePostAdapter.FavViewHolder>(PostAdapter.PostDiffCallback()) {

    inner class FavViewHolder(val binding: ItemFavoritePostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding =
            ItemFavoritePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val post = getItem(position)
        holder.binding.apply {
            title.text = post.name
            description.text = post.body
            deleteButton.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.remove_from_favorites))
                    .setMessage(context.getString(R.string.ask_remove_from_favorites))
                    .setPositiveButton("Yes") { _, _ -> onDeleteClick(post) }
                    .setNegativeButton("No", null)
                    .show()

            }
        }
    }
}