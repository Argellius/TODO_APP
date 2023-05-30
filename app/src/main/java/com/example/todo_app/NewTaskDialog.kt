import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.TaskListAdapter
import com.example.todo_app.recyclerview.TaskListAdapterCategory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.objectbox.Box


class NewTaskDialog(private val tasks_category: List<CategoryEntity>) : BottomSheetDialogFragment(), DialogInterface.OnDismissListener {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var taskBox: Box<TaskEntity>
    private var adapterRecyc: TaskListAdapter? = null
    private var adapterRecycCategory: TaskListAdapterCategory? = null
    private var recyclerView: RecyclerView? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        adapterRecyc?.notifyDataSetChanged()
        adapterRecycCategory?.notifyDataSetChanged()
        recyclerView?.scrollToPosition(0)

    }

    fun setAdapterRecyc(adapter: TaskListAdapter) {
        this.adapterRecyc = adapter
    }
    fun setAdapterRecycCategory(adapter: TaskListAdapterCategory) {
        this.adapterRecycCategory = adapter
    }


    fun setRecyclerView(rec: RecyclerView) {
        this.recyclerView = rec
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.add_task_item, container, false)
        // Initialize the EditText and Button views
        taskEditText = view.findViewById(R.id.task_edit_text) as EditText
        spinnerCategory = view.findViewById(R.id.spinner_category) as Spinner
        saveButton = view.findViewById(R.id.add_button) as Button
        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)

        val adapter = NewTaskCategoryAdapter(requireContext(), tasks_category)
        spinnerCategory.adapter = adapter

        // Set a click listener on the add button
        saveButton.setOnClickListener {
            // Get the task name from the EditText view
            val taskName = taskEditText.text.toString()
            val selectedCategory = spinnerCategory.selectedItem as CategoryEntity
            val selectedValue = selectedCategory.id
            // Create a new TaskEntity object and set its description property
            val taskEntity = TaskEntity()
            taskEntity.position = adapterRecyc?.getData()?.count()?.plus(1)?.toLong()!!
            taskEntity.description = taskName
            taskEntity.category = selectedValue
            adapterRecyc?.addData(taskEntity)
            // Add the TaskEntity object to the database
            taskBox.put(taskEntity)

            // Clear the EditText view
            taskEditText.text.clear()
        }
        return view
    }
}