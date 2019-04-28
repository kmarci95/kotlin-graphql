import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val firstName = varchar("firstName", 255) // Column<String>
    val lastName = varchar("lastName", 255) // Column<String>
    val email = varchar("email", 255) // Column<String>
}