package com.example.todo_app.recyclerview.Adapters

import CategoryEntity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.todo_app.R

class TaskFragmentNewTaskCategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryEntity>,
) : BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): CategoryEntity {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_text, parent, false)
        val textView = view.findViewById<TextView>(R.id.text1)
        val category = getItem(position)
        textView.text = category.description
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_dropdown, parent, false)
        val textView = view.findViewById<TextView>(R.id.text1)
        val category = getItem(position)
        textView.text = category.description
        return view
    }
}
