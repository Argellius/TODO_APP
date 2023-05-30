package com.example.todo_app.recyclerview

import CategoryEntity
import TaskEntity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import io.objectbox.Box
import java.util.*

class TaskListAdapterCategory(
    private var categoryDataSet: List<CategoryEntity>,
    private val categoryBox: Box<CategoryEntity>,
    private val context : Context,
    private val taskBox: Box<TaskEntity>
) :
    RecyclerView.Adapter<TaskListAdapterCategory.ViewHolder>() {

    private lateinit var myContext: Context

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDescription: TextView
        val textViewCountTasks: TextView

        init {
            // Define click listener for the ViewHolder's View
            textViewDescription = view.findViewById(R.id.description_text_view_category)
            textViewCountTasks = view.findViewById(R.id.count_tasks)
        }
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(categoryDataSet, fromPosition, toPosition)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(categoryDataSet, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(categoryDataSet, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
    fun setData(newData: List<CategoryEntity>) {
        categoryDataSet = newData
        notifyDataSetChanged()
    }

    fun getData(): List<CategoryEntity> {
        return categoryDataSet
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val taskEntity = categoryDataSet[position]


        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewDescription.text = taskEntity.description
        val tasksWithCategoryOne = taskBox.query()
            .equal(TaskEntity_.category, 1L)
            .build()
            .count()
        viewHolder.textViewCountTasks.text = tasksWithCategoryOne.toString() + " " + context.getString(R.string.tasks)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.task_item_category, viewGroup, false)

        myContext = context
        return ViewHolder(view)
    }


    fun updateTask(currentPosition: Int, newPosition: Int) {
        // Get the task entity at the current position
        val taskEntity = categoryDataSet[currentPosition]

        // Remove the task from the current position in the list
        categoryDataSet = categoryDataSet.toMutableList().apply { removeAt(currentPosition) }

        // Add the task at the new position in the list
        categoryDataSet = categoryDataSet.toMutableList().apply { add(newPosition, taskEntity) }.toList()

        // Update the positions of the tasks in the database
        for (i in categoryDataSet.indices) {
            val task = categoryDataSet[i]
            task.position = i.toLong()
            categoryBox.put(task)
        }

        // Notify the adapter of the move
        notifyItemMoved(currentPosition, newPosition)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = categoryDataSet.size

}
