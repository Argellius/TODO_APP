package com.example.todo_app.recyclerview

import CategoryEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import io.objectbox.Box
import java.util.*

class TaskListAdapterCategoryEdit(
    private var dataSet: List<CategoryEntity>,
    private val taskBox: Box<CategoryEntity>,
) :
    RecyclerView.Adapter<TaskListAdapterCategoryEdit.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.description_text_view)
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

    fun setData(newData: List<CategoryEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    fun getData(): List<CategoryEntity> {
        return dataSet
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val taskEntity = dataSet[position]


        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = taskEntity.description
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater
                .from(viewGroup.context)
                .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }


    fun updateTask(currentPosition: Int, newPosition: Int) {
        // Get the task entity at the current position
        val taskEntity = dataSet[currentPosition]

        // Remove the task from the current position in the list
        dataSet = dataSet.toMutableList().apply { removeAt(currentPosition) }

        // Add the task at the new position in the list
        dataSet = dataSet.toMutableList().apply { add(newPosition, taskEntity) }.toList()

        // Update the positions of the tasks in the database
        for (i in dataSet.indices) {
            val task = dataSet[i]
            task.position = i.toLong()
            taskBox.put(task)
        }

        // Notify the adapter of the move
        notifyItemMoved(currentPosition, newPosition)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    fun addData(item: CategoryEntity) {
        val updatedList = dataSet.toMutableList() // Vytvoření kopie stávajícího seznamu
        updatedList.add(0, item) // Přidání nové položky do kopie seznamu
        dataSet = updatedList.toList() // Přiřazení upraveného seznamu zpět do dataSet
        notifyItemInserted(0) // Upozornění adaptéru na vložení nové položky
    }

}
