package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.view_item.view.*


class todoAdapter(val context: Context, var todoList : ArrayList<Todo>, var Db: SQLiteDatabase) : BaseAdapter(){

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = p1 ?: li.inflate(R.layout.view_item, p2, false)  //If p1 is null, then the right side will be evalated and returned. it is the operation which i have already done.

        itemView.todoTextView.text = todoList[p0].task

        //Therefore if image view is clicked this function will be called
        itemView.doneImageView.setOnClickListener {
            val todoClicked = todoList[p0]
            todoClicked.doneOrNot = !todoClicked.doneOrNot
            todoTable.updateTodo(Db,todoClicked)
            todoList.clear()
            todoList.addAll(todoTable.getAllTodo(Db))
            notifyDataSetChanged()
        }


        itemView.cancelImageView.setOnClickListener {
            val todoClicked = todoList[p0]
            todoTable.deleteById(Db,todoClicked)
            todoList.clear()
            todoList.addAll(todoTable.getAllTodo(Db))
            notifyDataSetChanged()
        }

        if(todoList[p0].doneOrNot == true)
        {
            //itemView.findViewById<TextView>(R.id.todoTextView).setTextColor(Color.GRAY)
            itemView.doneImageView.setBackgroundResource(R.drawable.ic_check_circle_indigo_a700_24dp)
        }

        else
        {
            //It means task is undone till now
            itemView.findViewById<TextView>(R.id.todoTextView).setTextColor(Color.GRAY)
            itemView.doneImageView.setBackgroundResource(R.drawable.ic_check_circle_indigo_100_24dp)
        }
        return itemView
    }

    override fun getItem(p0: Int) = todoList[p0]

    override fun getItemId(p0 : Int): Long {
        return 0
    }

    override fun getCount() = todoList.size

    fun updateTasks(newTasks: ArrayList<Todo>) {
        todoList.clear()
        todoList.addAll(newTasks)
        notifyDataSetChanged()
    }
}