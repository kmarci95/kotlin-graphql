import org.jetbrains.exposed.sql.Table

object Categories : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val category = varchar("category", 255)
}