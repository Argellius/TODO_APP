import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import java.util.*

class TaskAdapter(private var dataSet: List<TaskEntity>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val checkbox : CheckBox
        val date : TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.description_text_view)
            checkbox = view.findViewById(R.id.is_done_checkbox)
            date = view.findViewById(R.id.date_text_view)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    fun setData(newData: List<TaskEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].description
        val displayDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(dataSet[position].dueDate)
        viewHolder.date.text = displayDate
        viewHolder.checkbox.isChecked = dataSet[position].isDone
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
