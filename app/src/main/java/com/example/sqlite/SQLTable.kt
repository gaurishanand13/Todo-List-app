package com.example.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.*
import kotlin.collections.ArrayList

object todoTable{

    //first we will give the name to this table
    val TABLE_NAME = "todos"


    val CMD_CREATE_TABLE = """CREATE TABLE IF NOT EXISTS todos
        (
         ID INTEGER PRIMARY KEY AUTOINCREMENT,
         TASK TEXT,
         DONE BOOLEAN
        );
        """.trimIndent()


    fun insertTodo(dataBase : SQLiteDatabase,todo: Todo)
    {
       //Here todoObject has the  data which we want to insert in the row of the table
        val row = ContentValues()
        //Now since we know the row , we can insert in each column one by one. Since we
        // have set the id as primary key which which will be autoIncremented. Therefore don't need to put it
        row.put("TASK",todo.task)
        row.put("DONE",todo.doneOrNot)


        ///Therefore to insert any row in the table we provide 3 things. One is the table name,
        // 2) second one is the nullColumnHack which is very difficult to understand for now. So read it from net.
        //3) 3rd one is the row which you want to insert
        dataBase.insert(TABLE_NAME,null,row) //therefore we pass the data for a raw in the form of contentValues
    }



    fun getAllTodo(dataBase: SQLiteDatabase) : ArrayList<Todo>{
        //Now to get the whole list from the table we will use the cursor.
        // Note- > initially cursor is at the 1st row and it moves down row by row when the cursor is moved to the nextPosition

        //Just like we do query in sql to find the data from the table. Here also we make a query
        var cursor : Cursor = dataBase.query(TABLE_NAME, arrayOf("ID","TASK","DONE" ), //It is the data of the columns of each row from which we want the cursor to take out the data.
            null,null,null,null,null)   //They are all the parameter which i have already seen in sql. Since here we don't want to filter the data as want the whole todolist therefore i have set all other filters as null

        val idCol = cursor.getColumnIndex("ID")
        val taskCol = cursor.getColumnIndex("TASK")
        val doneCol = cursor.getColumnIndex("DONE")

        //Since we want the whole data. Therefore we would make sure that we move the cursor to the first row
        cursor.moveToFirst()

        val wholeTodoList = ArrayList<Todo>()
        while(cursor.moveToNext()) //This cursor.moveToNext will become null as soon as the row finishes therefore then the while loop will stop
        {
            val todo = Todo(cursor.getInt(idCol),cursor.getString(taskCol),cursor.getInt(doneCol)==1)  //Here we are telling the cursor to get String from column with columnIndex = 1 and get int from the columnIdex = 2(int becuase we can't store boolean in an sqlite table directly, it will be stored as int only but then we can make as boolean like i have done)
            wholeTodoList.add(todo)
        }

        //generally after using the cursor. we should close it. It is a good practice
        cursor.close()
        return wholeTodoList
    }



    fun updateTodo(db : SQLiteDatabase , todo : Todo)
    {
        val todoRow = ContentValues()
        todoRow.put("TASK",todo.task)
        todoRow.put("DONE",todo.doneOrNot)

        //First we have made the new row, then we will replace the old row with the new row we have made, in which only difference is of the task,done or not
        db.update(TABLE_NAME,todoRow,"ID = ?", arrayOf(todo.id.toString()))
    }


    fun deleteById(db : SQLiteDatabase,todo: Todo)
    {
        db.delete(TABLE_NAME,"ID = ?", arrayOf(todo.id.toString()))
    }


    fun delete(db : SQLiteDatabase)
    {
        db.delete(TABLE_NAME,"DONE = ?", arrayOf("1"))
    }


    fun Sort(db: SQLiteDatabase)
    {

        val newList = ArrayList<Todo>()

        var cursor : Cursor = db.query(TABLE_NAME, arrayOf("ID","TASK","DONE" ),
            "DONE = ?",arrayOf("0"),null,null,null)


        val idCol = cursor.getColumnIndex("ID")
        val taskCol = cursor.getColumnIndex("TASK")
        val doneCol = cursor.getColumnIndex("DONE")

        while(cursor.moveToNext())
        {
            val todo = Todo(cursor.getInt(idCol),cursor.getString(taskCol),cursor.getInt(doneCol)==1)
            newList.add(todo)
        }

        cursor.close()

        cursor = db.query(TABLE_NAME, arrayOf("ID","TASK","DONE" ),
            "DONE = ?",arrayOf("1"),null,null,null)

        while(cursor.moveToNext())
        {
            val todo = Todo(cursor.getInt(idCol),cursor.getString(taskCol),cursor.getInt(doneCol)==1)
            newList.add(todo)
        }

        db.delete(TABLE_NAME,"DONE = ?", arrayOf("1"))
        db.delete(TABLE_NAME,"DONE = ?", arrayOf("0"))
        for(todo in newList)
        {
            val row = ContentValues()
            row.put("TASK",todo.task)
            row.put("DONE",todo.doneOrNot)
            db.insert(TABLE_NAME,null,row)
        }
    }


    fun search(db: SQLiteDatabase, s: CharSequence) : ArrayList<Todo>
    {

        val newList = ArrayList<Todo>()
        var cursor : Cursor = db.query(TABLE_NAME, arrayOf("ID","TASK","DONE" ),
            "TASK LIKE ?", arrayOf("%$s%"),null,null,null)

        val idCol = cursor.getColumnIndex("ID")
        val taskCol = cursor.getColumnIndex("TASK")
        val doneCol = cursor.getColumnIndex("DONE")

        while(cursor.moveToNext())
        {
            val todo = Todo(cursor.getInt(idCol),cursor.getString(taskCol),cursor.getInt(doneCol)==1)
            newList.add(todo)
        }
        return newList
    }
}