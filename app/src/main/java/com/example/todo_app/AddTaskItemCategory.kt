import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_app.R
import com.example.todo_app.database.ObjectBox
import io.objectbox.Box

class AddTaskItemCategory : AppCompatActivity() {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var categoryTaskBox: Box<CategoryEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task_item_category)

        // Initialize the EditText and Button views
        taskEditText = findViewById(R.id.task_edit_text)
        saveButton = findViewById(R.id.save_button)
        categoryTaskBox = ObjectBox.store.boxFor(CategoryEntity::class.java)

        // Set a click listener on the save button
        saveButton.setOnClickListener {
            // Get the task name from the EditText view
            val taskName = taskEditText.text.toString()

            val te : CategoryEntity = CategoryEntity()
            te.description = taskName
            categoryTaskBox.put(te)
            // Clear the EditText view
            taskEditText.text.clear()


        }
    }
}
