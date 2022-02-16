package com.example.workproject.api
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("activity/")
    fun getPosts(): Call<PostModel>
}

