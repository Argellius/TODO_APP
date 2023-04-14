import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.lang.reflect.Constructor
import java.util.*

@Entity
data class TaskEntity(
    @Id var id: Long = 0,
    var description: String,
    var category: Int,
    var dueDate: Date?,
    var priority: Int,
    var notification: Boolean,
    var notificationTime: String?,
    var note: String?,
    var isDone : Boolean = false,
    var position : Long?


){
    constructor() : this(0, "", 0, null, 0, false, null, null, false, null)
}