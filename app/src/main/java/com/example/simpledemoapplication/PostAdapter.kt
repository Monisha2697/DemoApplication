package com.example.simpledemoapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpledemoapplication.databinding.ItemPostBinding

class PostAdapter(private val onFavoriteClick: (Post) -> Unit) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {
   inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.binding.apply {
            title.text = post.name
            description.text = post.body

            cvPost.setOnClickListener {

                Log.d("FavoriteClick", "Clicked post ID: ${post.id} - ${post.name}")
                onFavoriteClick(post) }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }
}
