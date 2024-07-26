package com.example.todolist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TASKNAME_COl + " TEXT," +
                DONE_COL + " BOOLEAN" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addTask(task:Task){
        val values = ContentValues()
        values.put(TASKNAME_COl, task.taskInfo)
        values.put(DONE_COL, task.isDone)
        val db = this.writableDatabase
        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)
        // at last we are
        // closing our database
        db.close()
    }
    //delete list of task has id which chosen by user
    fun deleteTask(ids:List<String>){
        val db = this.writableDatabase
        if(db!=null){
            for(i:String in ids) db.delete(TABLE_NAME,"id=?", arrayOf(i))
            db.close()
        }
    }

    //Get all task from storage
    @SuppressLint("Range")
    fun getList():MutableList<Task> {
        val list = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        if (cursor != null) {
            try {
                cursor.moveToFirst()
                val task = Task(
                    cursor.getString(cursor.getColumnIndexOrThrow(ID_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TASKNAME_COl)),
                    convert(cursor.getInt(cursor.getColumnIndexOrThrow(DONE_COL)))
                )
                list.add(task)
                while (cursor.moveToNext()) {
                    val task = Task(
                        cursor.getString(cursor.getColumnIndexOrThrow(ID_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TASKNAME_COl)),
                        convert(cursor.getInt(cursor.getColumnIndexOrThrow(DONE_COL)))
                    )
                    list.add(task)
                }
            }catch (e:Exception){

            }
                cursor.close()
    }
        db.close()
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
