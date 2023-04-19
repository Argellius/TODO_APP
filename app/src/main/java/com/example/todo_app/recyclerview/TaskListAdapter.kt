package com.example.todo_app.recyclerview

import TaskEntity
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.system.Os.remove
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.TaskNew
import com.example.todo_app.Task_view
import io.objectbox.Box
import kotlinx.coroutines.currentCoroutineContext
import java.util.*

class TaskListAdapter(
    private var dataSet: List<TaskEntity>,
    private val taskBox: Box<TaskEntity>,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val checkbox: CheckBox
        val date: TextView
        val note: EditText
        val delete_button: ImageView
        val edit_button: ImageView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.description_text_view)
            checkbox = view.findViewById(R.id.is_done_checkbox)
            date = view.findViewById(R.id.date_text_view)
            note = view.findViewById(R.id.edit_text)
            delete_button = view.findViewById(R.id.delete_note_button)
            edit_button = view.findViewById(R.id.edit_note_button)

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
                .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    fun setData(newData: List<TaskEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    fun getData(): List<TaskEntity> {
        return dataSet
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val taskEntity = dataSet[position]


        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = taskEntity.description
        if (taskEntity.dueDate != null)
            viewHolder.date.text =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(taskEntity.dueDate)
        viewHolder.checkbox.isChecked = taskEntity.isDone


        viewHolder.checkbox.setOnCheckedChangeListener { _, isChecked ->

            var continue_: Boolean = true;

            taskEntity.isDone = isChecked

            taskBox.put(taskEntity)

            if (taskEntity.isDone == true) {
                // Remove the unchecked task from the current position
                dataSet =
                    dataSet.toMutableList().apply { removeAt(taskEntity.position!!.toInt()) }
                // Add the unchecked task to the end of the list
                dataSet = dataSet.toMutableList().apply { add(taskEntity) }
                // Notify the RecyclerView that the item has been moved
                notifyItemMoved(taskEntity.position!!.toInt(), dataSet.size - 1)
            }


            // Update the positions of the tasks in the database
            for (i in dataSet.indices) {
                val task = dataSet[i]
                task.position = i.toLong()
                taskBox.put(task)
            }


            var task = fragmentManager.findFragmentByTag("Task_view") as? Task_view
            task?.scrollRecyclerViewToPosition(0)


        }


        viewHolder.note.setText(dataSet[position].note)
        viewHolder.note.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                taskEntity.note = s.toString()
                taskBox.put(taskEntity)
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })

        viewHolder.delete_button.setOnClickListener {
            taskBox.remove(taskEntity)
            dataSet = dataSet.toMutableList().apply {
                remove(taskEntity)
            }
            notifyItemRemoved(position)
        }

        viewHolder.edit_button.setOnClickListener {
            val fragment = TaskNew()

            // Vytvoření Bundle a předání dat z aktivity do fragmentu
            val bundle = Bundle()
            bundle.putInt("id", taskEntity.id.toInt())
            bundle.putString("description", taskEntity.description)
            fragment.arguments = bundle

            // Otevření fragmentu
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

//    fun updateTask(currentPosition: Int, newPosition: Int) {
//
//        val currentPositionMutable = if (currentPosition == 0) currentPosition + 1 else currentPosition
//
//        // získání položek z databáze na daných pozicích
//        val taskEntity1: TaskEntity = taskBox.get(currentPositionMutable.toLong())
//        val taskEntity2: TaskEntity = taskBox.get(newPosition.toLong())
//
//// vyměna pozic v databázi
//        taskBox.put(taskEntity1.apply { position = newPosition.toLong() })
//        taskBox.put(taskEntity2.apply { position = currentPositionMutable.toLong() })
//
//// aktualizace seznamu v adapteru
//        notifyItemMoved(currentPositionMutable.toInt(), newPosition.toInt())
//
//    }

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

}
