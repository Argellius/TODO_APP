package com.example.todo_app

import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.TaskListAdapter
import com.example.todo_app.recyclerview.TaskListAdapterCategory
import io.objectbox.Box

class Need_to_do_task : Fragment() {

    private var allValidTask: List<TaskEntity>? = null
    private var actPositon = 0
    private lateinit var taskBox: Box<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_need_to_do_task, container, false)

        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)
        val recyclerView: RecyclerView? = v?.findViewById(R.id.task_list)
        val recyclerView_category: RecyclerView? = v?.findViewById(R.id.task_list_category)

        // Vytvoříme instanci adaptéru s prázdným seznamem
        val taskListAdapter = TaskListAdapter(emptyList(), taskBox )
        val taskListAdapter_category = TaskListAdapterCategory(emptyList(), taskBox, requireActivity().supportFragmentManager, null )

        // Přidáme položky do seznamu
        val tasks = taskBox.all
            .sortedBy { it.position }

        // Přidáme položky do seznamu
        val tasks2 = taskBox.all
            .sortedBy { it.position }

        taskListAdapter.setData(tasks)
        taskListAdapter_category.setData(tasks2)

        // Předáme instanci adaptéru k RecyclerView
        recyclerView?.adapter = taskListAdapter
        recyclerView_category?.adapter = taskListAdapter_category

        //Layouty
        val layoutManager = LinearLayoutManager(activity)
        val layoutManager_category = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = layoutManager

        recyclerView_category?.layoutManager = layoutManager_category


        return v
    }

//    private fun setTask(firstTask: TaskEntity?) {
//        if (firstTask != null) {
//
//            descriptionTextView.text = firstTask.description
//            noteTextView.text = firstTask.note
//
//            if (TextUtils.isEmpty(firstTask.note))
//            {
//                note_button.setEnabled(false)
//                noteTextView.visibility = View.GONE
//                note_button.setTextColor(Color.GRAY);
//                note_button.setBackgroundColor(getResources().getColor(R.color.darkYellow))
//
//            }
//            else {
//                note_button.setEnabled(true)
//                noteTextView.visibility = View.VISIBLE
//                note_button.setTextColor(Color.BLACK);
//                note_button.setBackgroundColor(getResources().getColor(R.color.myYellow))
//
//            }
//
//            val sdf = SimpleDateFormat("dd.MM.yyyy")
//            if (firstTask.dueDate != null) {
//                val dateStr = sdf.format(firstTask.dueDate)
//                dateTextView.text = dateStr
//            } else
//                dateTextView.visibility = View.GONE
//
//        } else {
//            descriptionTextView.text = "You have completed everything, you are amazing!\n"
//            dateTextView.visibility = View.GONE
//            noteTextView.visibility = View.GONE
//            complete_button.visibility = View.GONE
//            note_button.visibility = View.GONE
//            delete_button.visibility = View.GONE
//
//        }
//
//        note_button.setOnClickListener {
//            if (noteTextView.isVisible)
//                noteTextView.visibility = View.GONE
//            else
//                noteTextView.visibility = View.VISIBLE
//        }
//
//        delete_button.setOnClickListener {
//            firstTask?.isDone = true
//            taskBox.put(firstTask)
//            val firstTask = taskBox.all.filter { !it.isDone }
//                .sortedBy { it.position }
//                .firstOrNull()
//
//
//            setTask(firstTask)
//
//        }
//
//    }
    fun swapToRight() {
    if (actPositon >= (allValidTask?.size?.minus(1) ?: -1)) {
        return
    }
        actPositon += 1
        //setTask(allValidTask?.get(actPositon))
    }

    fun swapToLeft() {
        if (actPositon <= 0) {
            return
        }
        actPositon -= 1
        //setTask(allValidTask?.get(actPositon))
    }
}