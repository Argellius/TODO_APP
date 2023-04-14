package com.example.todo_app

import TaskEntity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.todo_app.database.ObjectBox
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Need_to_do_task.newInstance] factory method to
 * create an instance of this fragment.
 */
class Need_to_do_task : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_need_to_do, container, false)
        val descriptionTextView: TextView = v.findViewById(R.id.description_text_view)
        val dateTextView: TextView = v.findViewById(R.id.date_text_view)


        val taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)
        val firstTask = taskBox.all.filter { !it.isDone }
                                    .sortedBy { it.position }
                                    .firstOrNull()
        if (firstTask != null )
        {

            descriptionTextView.text = firstTask.description
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            if (firstTask.dueDate != null)
            {
                val dateStr = sdf.format(firstTask.dueDate)
                dateTextView.text = dateStr
            }
        }
        else {
            descriptionTextView.text = "You have completed everything, you are amazing!\n"
            dateTextView.text = ""
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Need_to_do_task().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}