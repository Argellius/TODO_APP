package com.example.todo_app

import CategoryEntity
import NewTaskDialog
import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.TaskListAdapter
import com.example.todo_app.recyclerview.TaskListAdapterCategory
import com.google.android.material.imageview.ShapeableImageView
import io.objectbox.Box


class Need_to_do_task : Fragment() {

    private var allValidTask: List<TaskEntity>? = null
    private var actPositon = 0
    private lateinit var taskBox: Box<TaskEntity>
    private lateinit var categoryBox: Box<CategoryEntity>
    private lateinit var mTaskNewButton: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_need_to_do_task, container, false)

        mTaskNewButton = v?.findViewById(R.id.button_add_task) as ShapeableImageView

        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)
        categoryBox = ObjectBox.store.boxFor(CategoryEntity::class.java)
        val recyclerView: RecyclerView? = v?.findViewById(R.id.RecyclerView_todays_task)
        val recyclerView_category: RecyclerView? = v?.findViewById(R.id.RecyclerView_category)

        // Vytvoříme instanci adaptéru s prázdným seznamem
        val taskListAdapter = TaskListAdapter(emptyList(), taskBox )
        val taskListAdapter_category = TaskListAdapterCategory(emptyList(), categoryBox )

        // Přidáme položky do seznamu
        val tasks = taskBox.all
            .sortedBy { it.position }

        // Přidáme položky do seznamu
        val tasks_category = categoryBox.all
            .sortedBy { it.position }

        taskListAdapter.setData(tasks)
        taskListAdapter_category.setData(tasks_category)

        // Předáme instanci adaptéru k RecyclerView
        recyclerView?.adapter = taskListAdapter
        recyclerView_category?.adapter = taskListAdapter_category

        //Layouty
        val layoutManager = LinearLayoutManager(activity)
        val layoutManager_category = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = layoutManager

        recyclerView_category?.layoutManager = layoutManager_category

        mTaskNewButton.setOnClickListener(View.OnClickListener {
            val dialog = NewTaskDialog()
            dialog.show(childFragmentManager, "NewTaskDialog")
        })


        return v
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