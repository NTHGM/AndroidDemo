package com.example.todolist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    public lateinit var adapter:ViewAdapter
    lateinit var inputText : EditText
    public lateinit var addBut:Button
    public lateinit var deleteBut:Button
    public lateinit var reloadBut:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up adapter and list
        adapter = ViewAdapter(mutableListOf(), this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //load data from storage and display on UI
        adapter.reload()

        //regist UI element
        inputText = findViewById(R.id.input)
        addBut = findViewById(R.id.addBut)
        deleteBut = findViewById(R.id.deleteBut)
        reloadBut = findViewById(R.id.refresh)

        reloadBut.setOnClickListener{
            adapter.reload()
        }

        addBut.setOnClickListener{
            val taskName = inputText.text.toString()
            if(!taskName.trim().isEmpty()){
                val task = Task("",taskName)
                adapter.addTask(task)
            }
            inputText.text.clear()
        }

        deleteBut.setOnClickListener{
            adapter.deleteDoneTask()
        }

    }
}
