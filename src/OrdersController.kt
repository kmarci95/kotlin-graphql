import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.print.Book
import java.util.ArrayList

class OrdersController() : Controller<OrderData>() {

    override fun update(data: OrderData): MutableList<OrderData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun create(id: Int?, userId: Int, bookIds: MutableList<Int>?, book: BookData?): OrderData {
        return OrderData(id, userId, bookIds, book);
    }

    fun getOrders(): MutableList<OrderData> {
        val ordersData: MutableList<OrderData> = ArrayList()

        transaction {
            (Orders innerJoin OrdersBooks innerJoin Books innerJoin Categories).selectAll().forEach {
                print(it)
                ordersData.add(
                    create(
                        it[Orders.id],
                        it[Orders.userId],
                        null,
                        BookData(it[Books.id],
                            it[Books.title],
                            it[Books.description],
                            it[Books.author],
                            it[Books.price],
                            it[Books.categoryId],
                            CategoryData(it[Categories.id], it[Categories.category])
                            )
                    )
                )
            }
        }

        return ordersData
    }

    fun getOrder(id: Int): MutableList<OrderData> {
        val ordersData: MutableList<OrderData> = ArrayList()

        transaction {
            (Orders innerJoin OrdersBooks innerJoin Books innerJoin Categories).select{Orders.id eq id}.forEach {
                print(it)
                ordersData.add(
                    create(
                        it[Orders.id],
                        it[Orders.userId],
                        null,
                        BookData(it[Books.id],
                            it[Books.title],
                            it[Books.description],
                            it[Books.author],
                            it[Books.price],
                            it[Books.categoryId],
                            CategoryData(it[Categories.id], it[Categories.category])
                        )
                    )
                )
            }
        }

        return ordersData
    }

    fun getUserOrders(id: Int): MutableList<OrderData> {
        val ordersData: MutableList<OrderData> = ArrayList()

        transaction {
            (Orders innerJoin Users innerJoin OrdersBooks innerJoin Books innerJoin Categories).select{Users.id eq id}.forEach {
                print(it)
                ordersData.add(
                    create(
                        it[Orders.id],
                        it[Orders.userId],
                        null,
                        BookData(it[Books.id],
                            it[Books.title],
                            it[Books.description],
                            it[Books.author],
                            it[Books.price],
                            it[Books.categoryId],
                            CategoryData(it[Categories.id], it[Categories.category])
                        )
                    )
                )
            }
        }

        return ordersData
    }

    override fun get(id: Int?): MutableList<OrderData> {
        if(id != null) {
            return getOrder(id)
        }
        return getOrders()
    }

    override fun post(data: OrderData): OrderData {
        var id: Int = 0
        print(data)

        transaction {
            id = Orders.insert {
                it[userId] = data.userId
            } get Orders.id

            OrdersBooks.batchInsert(data.bookIds!!) {bookId ->
                this[OrdersBooks.orderId] = id
                this[OrdersBooks.bookId] = bookId
            }
        }

        return OrderData(id, data.userId, data.bookIds)
    }

//    override fun update(data: UserData): MutableList<UserData> {
//        transaction {
//            val id: Int = data.id!!
//            Users.update({Users.id eq id}) {
//                it[Users.firstName] = data.firstName
//                it[Users.lastName] = data.lastName
//                it[Users.email] = data.email
//            }
//        }
//
//        return getUser(data.id!!)
//    }
//
//    override fun delete(id: Int) {
//        transaction {
//            Users.deleteWhere { Users.id eq id }
//        }
//    }
}