package com.example.todolist.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//creat api
object RetrofitInstance {
    //if using dagger hilt. dont need this

    val api:Api by lazy{
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}