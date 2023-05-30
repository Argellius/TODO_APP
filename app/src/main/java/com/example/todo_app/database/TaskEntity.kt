import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.Date

@Entity
data class TaskEntity(
    @Id(assignable = true) var id: Long = 0,
    var description: String,
    var category: Long = 0,
    var dueDate: Date?,
    var priority: Int,
    var notification: Boolean,
    var notificationTime: String?,
    var note: String?,
    var isDone: Boolean = false,
    var position: Long
) {
    constructor() : this(0, "", 0, null, 0, false, null, null, false, 0)
}