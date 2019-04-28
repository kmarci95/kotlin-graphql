import org.jetbrains.exposed.sql.Table

object Orders : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val userId = (integer("userId") references Users.id)
}