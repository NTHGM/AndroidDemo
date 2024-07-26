package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ViewAdapter(var taskList:MutableList<Task>,val mainContext:MainActivity):
    RecyclerView.Adapter<ViewAdapter.TaskViewHolder>(){
        lateinit var db:DBHelper
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
                    db = DBHelper(mainContext.baseContext,null)
                    db.updateIsDone(cur)
                    db.close()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size;
    }

    fun addTask(task: Task) {
        db = DBHelper(mainContext.baseContext, null)
        db.addTask(task)
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
        db.close()
    }

    fun deleteDoneTask() {
        val tmp = mutableListOf<String>()
        for(t:Task in taskList){
            if(t.isDone){
                tmp.add(t.id)
            }
        }
        db = DBHelper(mainContext.baseContext, null)
        db.deleteTask(tmp)
        tmp.clear()
        taskList.removeAll { task ->
            task.isDone
        }
        notifyDataSetChanged()
        db.close()
    }

    fun reload(){
        db = DBHelper(this.mainContext.baseContext, null)
        taskList = db.getList()
        notifyDataSetChanged()
        db.close()
    }
}