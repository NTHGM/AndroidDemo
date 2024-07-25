package com.example.todolist

data class Task(
    public var taskInfo:String,
    public var isDone:Boolean = false
){
    private lateinit var id:String
}
