package com.example.todo_app.dialog

import CategoryEntity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.Adapters.CategoryFragmentCategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.objectbox.Box


class NewCategoryDialog : BottomSheetDialogFragment() {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var categoryBox: Box<CategoryEntity>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterRecyc: CategoryFragmentCategoryAdapter

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        adapterRecyc.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)

    }

    fun setAdapterRecyc(adapter: CategoryFragmentCategoryAdapter) {
        this.adapterRecyc = adapter
    }

    fun setRecyclerView(rec: RecyclerView) {
        this.recyclerView = rec
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.add_task_item_category, container, false)
        // Initialize the EditText and Button views
        taskEditText = view.findViewById(R.id.category_edit_text) as EditText
        saveButton = view.findViewById(R.id.add_button) as Button
        categoryBox = ObjectBox.store.boxFor(CategoryEntity::class.java)

        // Set a click listener on the add button
        saveButton.setOnClickListener {
            // Get the task name from the EditText view
            val taskName = taskEditText.text.toString()

            // Create a new TaskEntity object and set its description property
            val taskEntity = CategoryEntity()
            taskEntity.description = taskName
            taskEntity.position = adapterRecyc.getData().count().plus(1).toLong()
            taskEntity.description = taskName
            adapterRecyc.addData(taskEntity)

            // Add the TaskEntity object to the database
            categoryBox.put(taskEntity)

            // Clear the EditText view
            taskEditText.text.clear()
        }
        return view
    }
}