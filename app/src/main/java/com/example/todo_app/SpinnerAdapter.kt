package com.example.todo_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(context: Context, private val items: Array<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item_text, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_item_text, parent, false
        )

        view.findViewById<TextView>(R.id.spinner_item_text).text = items[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_item_text, parent, false
        )

        view.findViewById<TextView>(R.id.spinner_item_text).text = items[position]
        return view
    }
}