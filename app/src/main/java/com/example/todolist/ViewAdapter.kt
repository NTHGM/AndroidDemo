package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ViewAdapter(var taskList:MutableList<Task>,val db:DBHelper):
    RecyclerView.Adapter<ViewAdapter.TaskViewHolder>(){
    class TaskViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val textView:TextView = itemView.findViewById(R.id.taskName)
        val check:CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val from = LayoutInflater.from(parent.context).inflate(R.layout.task,parent,false)
        return TaskViewHolder(from)
    }
    //create bug when done or undone task
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val cur = taskList[position]
        holder.apply {
            textView.text = cur.taskInfo
            check.isChecked = cur.isDone
            check.setOnCheckedChangeListener{
                    _, _ ->done(cur)
            }
        }
    }

    fun done(task:Task){
        task.isDone=!task.isDone
        //db.updateIsDone(task)
    }

    override fun getItemCount(): Int {
        return taskList.size;
    }

    fun addTask(task: Task) {
        db.addTask(task)
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }

    fun deleteDoneTask() {
        val tmp = mutableListOf<String>()
        for(t:Task in taskList){
            if(t.isDone){
                tmp.add(t.id)
            }
        }
        db.deleteTask(tmp)
        tmp.clear()
        taskList.removeAll { task ->
            task.isDone
        }
        notifyDataSetChanged()
    }

    fun reload(){
        taskList.clear()
        db.getList(taskList)
        notifyDataSetChanged()
    }
}