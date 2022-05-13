package com.example.android_mvc.network.service

import com.example.android_advanced_kotlin.activity.model.Post
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    fun listPost(): Call<ArrayList<Post>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Post>

}