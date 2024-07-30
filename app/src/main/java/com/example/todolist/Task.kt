package com.example.todolist

data class Task(
    public var id:String,
    public var taskInfo:String,
    public var isDone:Boolean = false
){
    constructor(taskInfo: String) : this("",taskInfo) {
        this.taskInfo = taskInfo
    }
}
