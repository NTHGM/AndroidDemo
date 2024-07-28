package com.example.todolist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    //this and onUpgrade are called
   // when getReadable or gerWriteable is call for the first time
    override fun onCreate(db: SQLiteDatabase) {
        //sqlite has a hide auto-increment column name rowid,
        // and INTEGER PRIMARY KEY is a alias of rowid
        // and both are the only type not allowing null values
        // while others variable-type allow null if you dont use NOT NULL
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TASKNAME_COl + " TEXT NOT NULL," +
                DONE_COL + " BOOLEAN NOT NULL" + ")")
        //use this if you dont want the database reuse the id
        val query1 = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASKNAME_COl + " TEXT NOT NULL," +
                DONE_COL + " BOOLEAN NOT NULL" + ")")
        db.execSQL(query)
    }
    //only call this when it has the change of the version(p1,p2)
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }

    // This method is for adding data in our database
    fun addTask(task:Task){
        try{
            val values = ContentValues()
            values.put(TASKNAME_COl, task.taskInfo)
            values.put(DONE_COL, task.isDone)
            val db = this.writableDatabase
            db.insert(TABLE_NAME, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    //delete list of task has id which chosen by user
    fun deleteTask(ids:List<String>){
        try {
            val db = this.writableDatabase
            for(i:String in ids) db.delete(TABLE_NAME,"id=?", arrayOf(i))
        }catch(e:Exception){
            e.printStackTrace()
        }
    }

    fun updateIsDone(task:Task){
        try{
            val db = this.writableDatabase
            val value = ContentValues()
            var x = 1
            if(task.isDone==false) x=0
            value.put(DONE_COL,x)
            db.update(TABLE_NAME, value, "id=?", arrayOf(task.id))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //Get all task from storage
    @SuppressLint("Range")
    fun getList(list : MutableList<Task>):MutableList<Task> {
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val task = Task(
                    cursor.getString(cursor.getColumnIndex(ID_COL)),
                    cursor.getString(cursor.getColumnIndex(TASKNAME_COl)),
                    convert(cursor.getInt(cursor.getColumnIndex(DONE_COL)))
                )
                list.add(task)
                while (cursor.moveToNext()) {
                    val task = Task(
                        cursor.getString(cursor.getColumnIndex(ID_COL)),
                        cursor.getString(cursor.getColumnIndex(TASKNAME_COl)),
                        convert(cursor.getInt(cursor.getColumnIndex(DONE_COL)))
                    )
                    list.add(task)
                }
                cursor.close()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return list
    }

    //convert from int to boolean cause cursor doesnt have getBool fun
    fun convert(a:Int):Boolean{
        if(a==1) return true
        return false
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "TodoDatabase"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "table_task"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val TASKNAME_COl = "task"

        // below is the variable for age column
        val DONE_COL = "isDone"
    }
}
