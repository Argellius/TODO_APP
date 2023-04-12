package com.example.todo_app

import TaskAdapter
import TaskEntity
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Entity
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Task_view.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        val taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)

        recyclerView = v.findViewById<RecyclerView>(R.id.task_list)
        // Vytvoříme instanci adaptéru s prázdným seznamem
        val taskListAdapter = TaskAdapter(emptyList())

        val format = SimpleDateFormat("dd.MM.yyyy")

// Přidáme položky do seznamu
        val tasks = taskBox.all
        taskListAdapter.setData(tasks)

// Předáme instanci adaptéru k RecyclerView
        recyclerView.adapter = taskListAdapter

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val button = v?.findViewById<FloatingActionButton>(R.id.floating_button)
        button?.setOnClickListener{

            Log.i("button", "button?.setOnClickListener{")
            val secondFragment = TaskNew()
            Log.i("button", "secondFragment{")
            val transaction = fragmentManager?.beginTransaction();
            Log.i("button", "transaction")
            transaction?.replace(com.example.todo_app.R.id.frame_layout,secondFragment)
            transaction?.commit()
        }


        return v;
    }
}