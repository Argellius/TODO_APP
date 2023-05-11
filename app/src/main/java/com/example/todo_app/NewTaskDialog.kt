import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.todo_app.R
import com.example.todo_app.database.ObjectBox
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.objectbox.Box


class NewTaskDialog : BottomSheetDialogFragment() {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var taskBox: Box<TaskEntity>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.add_task_item, container, false)
        // Initialize the EditText and Button views
        taskEditText = view?.findViewById(R.id.task_edit_text) as EditText
        saveButton = view?.findViewById(R.id.add_button) as Button
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
        return view
    }

    private fun saveTask(task: String) {
        // uložit úkol
        dismiss()
    }
}