package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.strictmode.SqliteObjectLeakedViolation
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_item.view.*
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    var todos = ArrayList<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //First we will create the database in writable format. In writable format we can read and write both. but when we open in readable format, we can only read
        val dataBaseHelper = myDataBaseHelper(this)
        val Db = dataBaseHelper.writableDatabase //Therefore here db is the database where we will store the data

        //Now to get the TodoList if already saved in the memory
        todos = todoTable.getAllTodo(Db)

        val adapter = todoAdapter(this,todos,Db)
        listView.adapter = adapter


        button.setOnClickListener {

           val newTodo = Todo(null,editText.text.toString(),false)

            todoTable.insertTodo(Db,newTodo)
            todos = todoTable.getAllTodo(Db)
            //Therefore we will make changes in this arrayList
            adapter.updateTasks(todos)

        }
        deleteButton.setOnClickListener {
            todoTable.delete(Db)
            todos = todoTable.getAllTodo(Db)
            adapter.updateTasks(todos)
        }

        sortButton.setOnClickListener {
            todoTable.Sort(Db)
            todos = todoTable.getAllTodo(Db)
            adapter.updateTasks(todos)
        }



        searchForUser.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                todos = todoTable.search(Db,p0!!)
                adapter.notifyDataSetChanged()
                adapter.updateTasks(todos)
            }

        })



    }
}
