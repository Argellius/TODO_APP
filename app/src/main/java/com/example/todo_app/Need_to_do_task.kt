package com.example.todo_app

import TaskEntity
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.todo_app.database.ObjectBox
import io.objectbox.Box
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [Need_to_do_task.newInstance] factory method to
 * create an instance of this fragment.
 */
class Need_to_do_task : Fragment() {

    private var allValidTask: List<TaskEntity>? = null
    private var actPositon = 0
    private lateinit var taskBox: Box<TaskEntity>
    private lateinit var descriptionTextView: TextView
    private lateinit var noteTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var note_button: Button
    private lateinit var delete_button: Button
    private lateinit var complete_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_need_to_do_task, container, false)

        descriptionTextView = v.findViewById(R.id.description_text_view)
        noteTextView = v.findViewById(R.id.note_text_view)
        dateTextView = v.findViewById(R.id.date_text_view)
        note_button = v.findViewById(R.id.note_button)
        delete_button = v.findViewById(R.id.delete_button)
        complete_button = v.findViewById(R.id.complete_button)

        var rlTop = v?.findViewById<LinearLayout>(R.id.llTop);
        rlTop?.setOnTouchListener(LinearLayoutTouchListener(requireActivity(), this));

        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)
        allValidTask = taskBox
            .all
            .filter { !it.isDone }
            .sortedBy { it.position }
        var firstTask : TaskEntity?;
        if (allValidTask?.size ?: 0 > 0)
            firstTask = allValidTask!!.getOrNull(actPositon)
        else
            firstTask = null
        setTask(firstTask)

        return v
    }

    private fun setTask(firstTask: TaskEntity?) {
        if (firstTask != null) {

            descriptionTextView.text = firstTask.description
            noteTextView.text = firstTask.note

            if (TextUtils.isEmpty(firstTask.note))
            {
                note_button.setEnabled(false)
                noteTextView.visibility = View.GONE
                note_button.setTextColor(Color.GRAY);
                note_button.setBackgroundColor(getResources().getColor(R.color.darkYellow))

            }
            else {
                note_button.setEnabled(true)
                noteTextView.visibility = View.VISIBLE
                note_button.setTextColor(Color.BLACK);
                note_button.setBackgroundColor(getResources().getColor(R.color.myYellow))

            }

            val sdf = SimpleDateFormat("dd.MM.yyyy")
            if (firstTask.dueDate != null) {
                val dateStr = sdf.format(firstTask.dueDate)
                dateTextView.text = dateStr
            } else
                dateTextView.visibility = View.GONE

        } else {
            descriptionTextView.text = "You have completed everything, you are amazing!\n"
            dateTextView.visibility = View.GONE
            noteTextView.visibility = View.GONE
            complete_button.visibility = View.GONE
            note_button.visibility = View.GONE
            delete_button.visibility = View.GONE

        }

        note_button.setOnClickListener {
            if (noteTextView.isVisible)
                noteTextView.visibility = View.GONE
            else
                noteTextView.visibility = View.VISIBLE
        }

        delete_button.setOnClickListener {
            firstTask?.isDone = true
            taskBox.put(firstTask)
            val firstTask = taskBox.all.filter { !it.isDone }
                .sortedBy { it.position }
                .firstOrNull()


            setTask(firstTask)

        }

    }


    public fun swapToRight() {
        if (actPositon >= allValidTask?.let { it.size - 1 } ?: -1) {
            return
        }
        actPositon += 1
        setTask(allValidTask?.get(actPositon))
    }

    public fun swapToLeft() {
        if (actPositon <= 0) {
            return
        }
        actPositon -= 1
        setTask(allValidTask?.get(actPositon))
    }
}