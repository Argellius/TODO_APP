package com.example.todo_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskNew.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskNew : Fragment() {
    private lateinit var selectedCalendar: Calendar
    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskDueDateEditText: EditText
    private lateinit var priorityRadioGroup: RadioGroup
    private lateinit var taskCategorySpinner: Spinner
    private lateinit var taskSaveButton: Button
    private lateinit var datePicker: EditText
    private lateinit var containerLayout: LinearLayout
    var priority: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_new, container, false)

        // Find view references
        containerLayout = view.findViewById(R.id.layout_notification)
        taskNameEditText = view.findViewById(R.id.edit_title)
        taskDescriptionEditText = view.findViewById(R.id.edit_description)
        taskDueDateEditText = view.findViewById(R.id.edit_due_date)
        taskCategorySpinner = view.findViewById(R.id.spinner_category)

        taskSaveButton = view.findViewById(R.id.button_save_task)

        val priorityRadioGroup = view?.findViewById<RadioGroup>(R.id.radio_group_priority)

        // Inicializace prvního EditText pole
        val editText1 = createEditText()
        containerLayout.addView(editText1)

        // Nastavení OnClickListener pro první EditText pole
        editText1.setOnClickListener {
            showTimePickerDialog(editText1)
        }


        // Set up category spinner
        val categories = arrayOf("Kategorie 1", "Kategorie 2", "Kategorie 3")
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskCategorySpinner.adapter = categoryAdapter

        priorityRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton? = view?.findViewById<RadioButton>(checkedId)
            priority = radio?.text.toString()
        }

        // Set up date picker
        selectedCalendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedCalendar.set(Calendar.YEAR, year)
            selectedCalendar.set(Calendar.MONTH, month)
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)
            taskDueDateEditText.setText(formattedDate)
        }

        taskDueDateEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set up save button
        taskSaveButton.setOnClickListener {
            saveTask()
        }

        return view
    }

    private fun saveTask() {
        // Retrieve values from form
        val name = taskNameEditText.text.toString()
        val description = taskDescriptionEditText.text.toString()
        val dueDate = taskDueDateEditText.text.toString()
        val priority = priority
        val category = taskCategorySpinner.selectedItem.toString()
        val date = datePicker.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.parse(date)

        // Save values to database or perform other actions as needed
        // ...

        // Clear form
        taskNameEditText.setText("")
        taskDescriptionEditText.setText("")
        taskDueDateEditText.setText("")
        priorityRadioGroup.check(R.id.radio_button_low)
        taskCategorySpinner.setSelection(0)
    }


    private fun createEditText(): EditText {
        val editText = EditText(requireContext())
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.inputType = InputType.TYPE_DATETIME_VARIATION_TIME
        editText.isFocusableInTouchMode = false
        editText.keyListener = null
        return editText
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)

                // Add a new EditText when the previous one is filled
                if (editText.text.isNotEmpty()) {
                    val newEditText = createEditText()
                    containerLayout.addView(newEditText)
                    newEditText.setOnClickListener {
                        showTimePickerDialog(newEditText)
                    }
                }
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }
}