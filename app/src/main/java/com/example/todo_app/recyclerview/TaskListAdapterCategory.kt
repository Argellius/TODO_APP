package com.example.todo_app.recyclerview

import TaskEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.Task_view
import io.objectbox.Box
import java.util.*

class TaskListAdapterCategory(
    private var dataSet: List<TaskEntity>,
    private val taskBox: Box<TaskEntity>,
    private val fragmentManager: FragmentManager,
    private val task_View: Task_view?
) :
    RecyclerView.Adapter<TaskListAdapterCategory.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.description_text_view_category) ?: throw NullPointerException("View must contain 'description_text_view_category'")
        }
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        Collections.swap(dataSet, fromPosition, toPosition)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataSet, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataSet, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.task_item_category, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskEntity = dataSet[position]


        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.textView.text = taskEntity.description
    }

    fun setData(newData: List<TaskEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    fun getData(): List<TaskEntity> {
        return dataSet
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
