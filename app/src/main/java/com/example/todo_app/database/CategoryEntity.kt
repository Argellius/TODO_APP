import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.lang.reflect.Constructor
import java.util.*

@Entity
data class CategoryEntity(
    @Id var id: Long = 0,
    var description: String,
    var activity : Int = 100,
    var position : Long = -1


){
    constructor() : this(0, "", 0, -1)
}