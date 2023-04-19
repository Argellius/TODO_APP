package com.example.todo_app

import TaskEntity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.todo_app.database.ObjectBox
import io.objectbox.Box
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


data class Category(val id: Int, val name: String)


/**
 * A simple [Fragment] subclass.
 * Use the [TaskNew.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskNew : Fragment() {
    private lateinit var selectedCalendar: Calendar
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskDueDateEditText: EditText
    private lateinit var priorityRadioGroup: RadioGroup
    private lateinit var taskCategorySpinner: Spinner
    private lateinit var taskSaveButton: Button
    private lateinit var datePicker: EditText
    private lateinit var containerLayout: LinearLayout
    private var countEditTextNotificationTime = 1
    private lateinit var categories: List<Category>
    var priority: String = ""
    private lateinit var myContext: Context
    private lateinit var switchNotification: Switch
    private lateinit var notificationTime: TextView
    private lateinit var taskBox: Box<TaskEntity>
    private var actualTaskEntity: TaskEntity? = null
    private var editTask: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskBox = ObjectBox.store.boxFor(TaskEntity::class.java)

        // vytvoření Coroutine scope
        val myScope = CoroutineScope(Dispatchers.Main)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_new, container, false)

        // Find view references
        containerLayout = view.findViewById(R.id.layout_notification)
        taskDescriptionEditText = view.findViewById(R.id.edit_description)
        taskDueDateEditText = view.findViewById(R.id.edit_due_date)
        taskCategorySpinner = view.findViewById(R.id.spinner_category)
        taskSaveButton = view.findViewById(R.id.button_save_task)
        switchNotification = view.findViewById<Switch>(R.id.switch_notification)
        priorityRadioGroup = view.findViewById<RadioGroup>(R.id.radio_group_priority)
        notificationTime = view.findViewById<TextView>(R.id.notification_time)


        val id = arguments?.getInt("id") ?: -1
        if (id != -1) {
            editTask = true
            actualTaskEntity = taskBox.get(id.toLong())
        }
        // získání hodnoty description z argumentů a přiřazení k taskDescriptionEditText
        if (actualTaskEntity != null)
            taskDescriptionEditText.setText(actualTaskEntity?.description)


        // Inicializace prvního EditText pole
        val editText1 = createEditText()
        containerLayout.addView(editText1)

        // Nastavení OnClickListener pro první EditText pole
        editText1.setOnClickListener {
            showTimePickerDialog(editText1)
        }


        // Set up category spinner
        categories = listOf(
            Category(1, "Kategorie 1"),
            Category(2, "Kategorie 2"),
            Category(3, "Kategorie 3")
        )
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories.map { it.name })
        taskCategorySpinner.adapter = categoryAdapter

        priorityRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton? = view.findViewById<RadioButton>(checkedId)
            priority = radio?.tag.toString()
        }

        // Set up date picker
        selectedCalendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedCalendar.set(Calendar.YEAR, year)
            selectedCalendar.set(Calendar.MONTH, month)
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val formattedDate =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)
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

            myScope.launch {
                saveTask()
            }
        }

        return view
    }

    private suspend fun saveTask() {

        if (editTask == false)
            actualTaskEntity = TaskEntity()
        var dueDate: Date? = null
        var priorityValue: String
        // Retrieve values from form
        if (TextUtils.isEmpty(taskDescriptionEditText.text)) {
            Toast.makeText(requireContext(), "Vyplňte description", Toast.LENGTH_LONG).show()
            return
        }

        val description = taskDescriptionEditText.text.toString()

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if (!TextUtils.isEmpty(taskDueDateEditText.text))
            dueDate = format.parse(taskDueDateEditText.text.toString())
        if (priority != null)
            priorityValue = priority

        val category = categories[taskCategorySpinner.selectedItemPosition].id
        val notificationEnabled = switchNotification.isChecked
        actualTaskEntity?.description
        actualTaskEntity?.description = description
        actualTaskEntity?.priority = if (TextUtils.isEmpty(priority)) -1 else priority.toInt()
        actualTaskEntity?.category = category
        actualTaskEntity?.dueDate = if (dueDate == null) null else dueDate
        actualTaskEntity?.notification = notificationEnabled
        actualTaskEntity?.notificationTime = null
        actualTaskEntity?.note = ""
        actualTaskEntity?.position = (taskBox.all.count() + 1).toLong()

        if (actualTaskEntity != null)
            taskBox.put(actualTaskEntity);

        // Clear form
        taskDescriptionEditText.setText("")
        taskCategorySpinner.setSelection(0)
        taskDueDateEditText.setText("")
        priorityRadioGroup.clearCheck()
        switchNotification.isChecked = false
        notificationTime.setText("")
        resetEditTexts()

        // návrat na předchozí fragment
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
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

    private fun resetEditTexts() {
        for (i in 0 until containerLayout.childCount) {
            val view = containerLayout.getChildAt(i)
            if (view is EditText) {
                view.text.clear()
            }
        }
        countEditTextNotificationTime = 1
        containerLayout.removeAllViews()
        containerLayout.addView(createEditText())
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
                if (editText.text.isNotEmpty() && countEditTextNotificationTime < 3) {
                    countEditTextNotificationTime++
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