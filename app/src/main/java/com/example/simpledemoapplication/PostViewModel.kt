package com.example.simpledemoapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    application: Application
) : AndroidViewModel(application) {
    private val prefs = SharedPrefManager(application)
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _favorites = MutableStateFlow<List<Post>>(emptyList())
    val favorites: StateFlow<List<Post>> = _favorites
    private val favoriteIds = mutableSetOf<Int>()

    init {
        viewModelScope.launch {
            try {
                val data = repository.getPosts()
                _posts.value = data
                prefs.savePosts(data)
                Log.d("PostViewModel", "Fetched posts: $data")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching posts", e)
            }
            favoriteIds.addAll(prefs.getFavorites())
            updateFavorites()
        }
    }

    fun toggleFavorite(post: Post): Boolean {
        if (favoriteIds.contains(post.id)) {
            favoriteIds.remove(post.id)
            prefs.saveFavorites(favoriteIds)
            updateFavorites()
            return false
        } else {
            favoriteIds.add(post.id)
            prefs.saveFavorites(favoriteIds)
            updateFavorites()
            return true
        }

    }

    private fun updateFavorites() {
        _favorites.value = _posts.value.filter { favoriteIds.contains(it.id) }
        Log.d("PostViewModel", "Updated favorites list: ${_favorites.value}")
    }

    fun refreshFavorites() {
        updateFavorites()
    }
}