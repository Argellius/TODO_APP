import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView: TextView = itemView.findViewById(R.id.description_text_view)
    private val checkBox: CheckBox = itemView.findViewById(R.id.is_done_checkbox)

    fun bind(item: TaskEntity) {
        titleTextView.text = item.description
        checkBox.isChecked = item.isDone
    }
}
