package com.pdp.coroutines.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.android_advanced_kotlin.activity.model.Post
import com.example.android_mvc.network.RetrofitHttp
import com.pdp.coroutines.R
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var tv_post: TextView
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        job = Job() // create the Job
    }

    override fun onDestroy() {
        job.cancel() // cancel the Job
        super.onDestroy()
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("TAG", "$exception handled !")
    }

    private fun initViews() {
        tv_post = findViewById(R.id.tv_post)

//        launch {
//            try {
//                supervisorScope {
//                    val firstPost = async { fetchFirstPost() }
//                    val secondPost = async { fetchSecondPost() }
//                    val first = try {
//                        firstPost.await()
//                    } catch (e: Exception) {
//                        Post(1, 1, "", "")
//                    }
//                    val second = try {
//                        secondPost.await()
//                    } catch (e: Exception) {
//                        Post(2, 1, "", "")
//                    }
//                }
//            } catch (exception: Exception) {
//                Log.d("TAG", "$exception handled !")
//            }
//        }

//        launch {
//            try {
//                val firstPost = async {  fetchFirstPost() }
//                val secondPost = async { fetchSecondPost() }
//                val first = firstPost.await()
//                val second = secondPost.await()
//            } catch (exception: Exception) {
//                Log.d("TAG", "$exception handled !")
//            }
//        }


        GlobalScope.launch(Dispatchers.Main + handler) {
            var post = fetchFirstPost() // do on IO thread and back to UI Thread
            showPost(post)
        }

//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                fetchFirstPost() // do on IO thread and back to UI Thread
//            } catch (exception: Exception) {
//                Log.d("TAG", "$exception handled !")
//            }
//        }

//        launch {
//            val userOne = async(Dispatchers.IO) { fetchFirstPost() }
//            val userTwo = async(Dispatchers.IO) { fetchSecondPost() }
//            showPosts(userOne.await(), userTwo.await())
//        }
    }

//    fun fetchAndShowPost() {
//        val response = RetrofitHttp.postService.getPost(1).execute()
//        showPost(response.body()!!)
//    }

//    fun fetchAndShowPost() {
//        RetrofitHttp.postService.getPost(1).enqueue(object : Callback<Post> {
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                val post = response.body()
//                showPost(post!!)
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//
//            }
//        })
//    }


    fun fetchAndShowPost() {
        GlobalScope.launch(Dispatchers.Main) {
            val post = fetchPost() // fetch on IO thread
            showPost(post) // back on UI thread
        }
    }

    suspend fun fetchPost(): Post {
        return GlobalScope.async(Dispatchers.IO) {
            // make network call
            val response = RetrofitHttp.postService.getPost(1).execute()
            // return post
            return@async response.body()!!
        }.await()
    }

    fun showPost(post: Post) {
        tv_post.text = post.toString()
    }

    suspend fun fetchFirstPost(): Post {
        return GlobalScope.async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.getPost(1).execute()
            return@async response.body()!!
        }.await()
    }

    suspend fun fetchSecondPost(): Post {
        return GlobalScope.async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.getPost(2).execute()
            return@async response.body()!!
        }.await()
    }

//    fun loadTwoPosts() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val postOne = withContext(Dispatchers.IO) { fetchFirstPost() }
//            val postTwo = withContext(Dispatchers.IO) { fetchSecondPost() }
//            showPosts(postOne, postTwo) // back on UI thread
//        }
//    }

    fun loadTwoPosts() {
        GlobalScope.launch(Dispatchers.Main) {
            val userOne = async(Dispatchers.IO) { fetchFirstPost() }
            val userTwo = async(Dispatchers.IO) { fetchSecondPost() }
            showPosts(userOne.await(), userTwo.await()) // back on UI thread
        }
    }

    fun showPosts(post1: Post, post2: Post) {
        tv_post.text = post1.toString() + post2.toString()
    }

}