import org.jetbrains.exposed.sql.Table

object Books : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val title = varchar("title", 255) // Column<String>
    val description = text("description") // Column<String>
    val author = varchar("author", 255) // Column<String>
    val price = integer("price") // Column<String>
    val categoryId = (integer("categoryId") references Categories.id)
}