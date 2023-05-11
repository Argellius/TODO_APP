package com.example.todo_app

import TaskEntity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_app.R
import com.example.todo_app.database.ObjectBox
import io.objectbox.Box

class AddTaskItemActivity : AppCompatActivity() {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var taskBox: Box<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task_item)

        // Initialize the EditText and Button views
        taskEditText = findViewById(R.id.task_edit_text)
        saveButton = findViewById(R.id.add_button)
        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)

        // Set a click listener on the add button
        saveButton.setOnClickListener {
            // Get the task name from the EditText view
            val taskName = taskEditText.text.toString()

            // Create a new TaskEntity object and set its description property
            val taskEntity = TaskEntity()
            taskEntity.description = taskName

            // Add the TaskEntity object to the database
            taskBox.put(taskEntity)

            // Clear the EditText view
            taskEditText.text.clear()
        }
    }
}
