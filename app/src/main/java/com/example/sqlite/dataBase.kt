package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



data class Todo(var id:Int?,var task:String,var doneOrNot:Boolean)


//We will extend this sqliteOpenHelper class which help us to manage the database eaily like if we want to create a database, or update the database and manage it or get something from it. It is very helpful
//Note - here the name of the dataBase created therefore will be "todos database
//version is unique here like if we change the way the data is stored in this database, the version code will change. our database changes
class myDataBaseHelper(context : Context) : SQLiteOpenHelper(context,"todos database",null,1){

    //This function is called for the first Time when the database is to be setUp. Therefore it will be executed when the user installs the app for the first time
    override fun onCreate(p0: SQLiteDatabase?) {

        //For our ease to create a table we have made the seperate class which will make the table for us
        p0?.execSQL(todoTable.CMD_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //this function is called when the database is upgraded i.e change in version code. For now we are not using it.
    }
}