package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ViewAdapter(val taskList:MutableList<Task>):
    RecyclerView.Adapter<ViewAdapter.TaskViewHolder>(){

    class TaskViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val textView:TextView = itemView.findViewById(R.id.taskName)
        val check:CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val from = LayoutInflater.from(parent.context).inflate(R.layout.task,parent,false)
        return TaskViewHolder(from)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val cur = taskList[position]
        holder.apply {
            textView.text = cur.taskInfo
            check.isChecked = cur.isDone
            check.setOnCheckedChangeListener{
                    _, _ ->
                run {
                    cur.isDone =!cur.isDone
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size;
    }

    fun addTask(task: Task) {
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }

    fun deleteDoneTask() {
        taskList.removeAll { task ->
            task.isDone
        }
        notifyDataSetChanged()
    }
}