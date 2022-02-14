package com.example.workproject
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("activity/")
    fun getPosts(): Call<PostModel>
}

