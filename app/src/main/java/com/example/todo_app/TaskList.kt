package com.example.todo_app

import CategoryEntity
import com.example.todo_app.dialog.NewTaskDialog
import TaskEntity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.Adapters.TaskListAdapter
import com.example.todo_app.recyclerview.Adapters.TaskFragmentCategoryAdapter
import com.google.android.material.imageview.ShapeableImageView
import io.objectbox.Box


class TaskList : Fragment() {

    private var allValidTask: List<TaskEntity>? = null
    private var actPositon = 0
    private lateinit var taskBox: Box<TaskEntity>
    private lateinit var categoryBox: Box<CategoryEntity>
    private lateinit var mTaskNewButton: ShapeableImageView
    private lateinit var recyclerViewTask: RecyclerView
    private lateinit var recyclerViewCategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_task_list, container, false)
        //Button save
        mTaskNewButton = v?.findViewById(R.id.button_add_task) as ShapeableImageView
        //Task Database
        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)
        //Category Database
        categoryBox = ObjectBox.store.boxFor(CategoryEntity::class.java)
        //RecycleView
        recyclerViewTask = v.findViewById(R.id.RecyclerView_todays_task)
        recyclerViewCategory = v.findViewById(R.id.RecyclerView_category)

        // Vytvoříme instanci adaptéru s prázdným seznamem
        val listAdapterTask = TaskListAdapter(emptyList(), taskBox )
        val listAdapterCategory =
            TaskFragmentCategoryAdapter(emptyList(), categoryBox, requireContext(), taskBox)

        // Přidáme položky do seznamu tasků
        val tasks = taskBox.all
            .sortedByDescending { it.position }

        for (task in tasks) {
            Log.d("TAG", "Task: $task")
        }

        // Přidáme položky do seznamu kategorií
        val tasks_category = categoryBox.all
            .sortedBy { it.position }

        listAdapterTask.setData(tasks)
        listAdapterCategory.setData(tasks_category)

        // Předáme instanci adaptéru k RecyclerView
        recyclerViewTask.adapter = listAdapterTask
        recyclerViewCategory.adapter = listAdapterCategory

        //Layouty
        val layoutManager = LinearLayoutManager(activity)
        val layoutManager_category = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTask.layoutManager = layoutManager
        recyclerViewCategory.layoutManager = layoutManager_category

        mTaskNewButton.setOnClickListener({
            val dialog = NewTaskDialog(tasks_category)
            dialog.setAdapterRecyc(listAdapterTask)
            dialog.setAdapterRecycCategory(listAdapterCategory)
            dialog.setRecyclerView(recyclerViewTask)
            dialog.show(childFragmentManager, "com.example.todo_app.dialog.NewTaskDialog")
        })


        return v
    }

    fun getRecyclerViewTask(): RecyclerView {
        return recyclerViewTask
    }
    fun swapToRight() {
    if (actPositon >= (allValidTask?.size?.minus(1) ?: -1)) {
        return
    }
        actPositon += 1
        //setTask(allValidTask?.get(actPositon))
    }

    fun swapToLeft() {
        if (actPositon <= 0) {
            return
        }
        actPositon -= 1
        //setTask(allValidTask?.get(actPositon))
    }
}