package com.example.todolist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    public lateinit var adapter:ViewAdapter
    lateinit var inputText : EditText
    public lateinit var addBut:Button
    public lateinit var deleteBut:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ViewAdapter(mutableListOf())
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        inputText = findViewById(R.id.input)
        addBut = findViewById(R.id.addBut)
        deleteBut = findViewById(R.id.deleteBut)

        addBut.setOnClickListener{
            val taskName = inputText.text.toString()
            if(!taskName.trim().isEmpty()){
                val task = Task(taskName)
                adapter.addTask(task)
            }
            inputText.text.clear()
        }

        deleteBut.setOnClickListener{
            adapter.deleteDoneTask()
        }
    }
}
