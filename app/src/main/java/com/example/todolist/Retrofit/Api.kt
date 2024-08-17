package com.example.todolist.Retrofit

import com.example.todolist.Task
import retrofit2.Response

import retrofit2.http.GET

interface Api {

    @GET("/todos")
    //if need using a key to call API: fun getTodos(@Query("key") key: String):Response<List<Task>>
    suspend fun getTodos():Response<List<Task>>//access in a corountin
    
   // @POST("/createTodo")
    //fun createTodo(@Body todo:Task): Response<CreatTodoRespone>
}