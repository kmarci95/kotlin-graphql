import org.jetbrains.exposed.sql.Table

object Books : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val title = varchar("name", 255) // Column<String>
    val categoryId = (integer("categoryId") references Categories.id)
}