import org.jetbrains.exposed.sql.Table

object OrdersBooks : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val orderId = (integer("orderId") references Orders.id)
    val bookId = (integer("bookId") references Books.id)
}