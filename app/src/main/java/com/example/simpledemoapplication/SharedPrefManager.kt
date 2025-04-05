package com.example.simpledemoapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun savePosts(posts: List<Post>) {
        val json = gson.toJson(posts)
        prefs.edit().putString("posts", json).apply()
    }

    fun getPosts(): List<Post> {
        val json = prefs.getString("posts", null) ?: return emptyList()
        val type = object : TypeToken<List<Post>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveFavorites(favoriteIds: Set<Int>) {
        prefs.edit().putStringSet("favorites", favoriteIds.map { it.toString() }.toSet()).apply()
    }

    fun getFavorites(): Set<Int> {
        return prefs.getStringSet("favorites", emptySet())!!.mapNotNull { it.toIntOrNull() }.toSet()
    }
}
