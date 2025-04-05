package com.example.simpledemoapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPosts(): List<Post> {
        return withContext(Dispatchers.IO) {
            apiService.getPosts()

        }
    }
}
