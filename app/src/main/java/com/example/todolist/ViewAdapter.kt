package com.example.todolist

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

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val cur = taskList[holder.bindingAdapterPosition]
        holder.apply {
            textView.text = cur.taskInfo
            check.isChecked = cur.isDone
            check.setOnClickListener{
                    done(cur)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size;
    }

    fun done(task:Task){
        try {
            db.updateIsDone(task)
            task.isDone=!task.isDone
        }catch (e:Exception){
            //show UI
            e.printStackTrace()
        }
    }

    fun addTask(task: Task) {
        try {
            db.addTask(task)
            taskList.add(task)
            notifyItemInserted(taskList.size - 1)
        }catch (e:Exception){
            //Show UI
            e.printStackTrace()
        }
    }

    //delete one task a time
    fun deleteDoneTask() {
        val tmp = mutableListOf<Task>()
        for(t:Task in taskList){
            if(t.isDone){
                tmp.add(t)
            }
        }
        for(t:Task in tmp){
            try {
                db.deleteTask(t.id)
                taskList.remove(t)
                notifyDataSetChanged()
            }catch (e:Exception){
                //Show UI
                e.printStackTrace()
                tmp.clear()
                return
            }
        }
    }

    //delete all
    fun deleteDoneTasks() {
        val tmp = mutableListOf<String>()
        for(t:Task in taskList){
            if(t.isDone){
                tmp.add(t.id)
            }
        }
        try {
            db.deleteTasks(tmp)
            tmp.clear()
            taskList.removeAll { task ->
                task.isDone
            }
            notifyDataSetChanged()
        }catch (e:Exception){
            //Show UI
            e.printStackTrace()
            reload()
        }
    }

    fun reload(){
        taskList.clear()
        try {
            taskList = db.getList()
            notifyDataSetChanged()
        }catch (e:Exception){
            //show UI
            e.printStackTrace()
        }
    }
}