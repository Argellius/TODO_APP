package com.example.todo_app

import ItemTouchHelperCallback
import com.example.todo_app.recyclerview.TaskListAdapter
import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox

class Task_view : Fragment() {

    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_task_view, container, false)

        //val floating_button = v?.findViewById<FloatingActionButton>(R.id.floating_button)
        recyclerView = v.findViewById<RecyclerView>(R.id.task_list)

        val taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)

        // Vytvoříme instanci adaptéru s prázdným seznamem
        val taskListAdapter = TaskListAdapter(emptyList(), taskBox )

        // Přidáme položky do seznamu
        val tasks = taskBox.all
            .sortedBy { it.position }

        taskListAdapter.setData(tasks)

        // Předáme instanci adaptéru k RecyclerView
        recyclerView.adapter = taskListAdapter

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Create the ItemTouchHelper and attach it to the RecyclerView
        val callback = ItemTouchHelperCallback(taskListAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        /*floating_button?.setOnClickListener{
            val secondFragment = TaskNew()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }*/


        return v
    }

    fun scrollRecyclerViewToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
        Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()

    }
}