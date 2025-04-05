package com.example.simpledemoapplication

import retrofit2.http.GET

interface ApiService {
    @GET("posts/1/comments")
    suspend fun getPosts(): List<Post>
}
