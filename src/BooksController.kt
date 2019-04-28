import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.print.Book
import java.util.ArrayList

class BooksController() : Controller<BookData>() {

    fun createBook(id: Int?, title: String, description: String, author: String, price: Int, categoryId: Int, category: CategoryData?): BookData {
        return BookData(id, title, description, author, price, categoryId, category);
    }

    fun getBooks(): MutableList<BookData> {
        val booksData: MutableList<BookData> = ArrayList()

        transaction {
            (Books innerJoin Categories).selectAll().forEach {
                val category = CategoryData(it[Categories.id], it[Categories.category])
                booksData.add(createBook(
                    it[Books.id],
                    it[Books.title],
                    it[Books.description],
                    it[Books.author],
                    it[Books.price],
                    it[Books.categoryId],
                    category
                ))
            }
        }

        return booksData
    }

    fun getBook(id: Int): MutableList<BookData> {
        val booksData: MutableList<BookData> = ArrayList()

        transaction {
            (Books innerJoin Categories).select{Books.id eq id}.forEach {
                val category = CategoryData(it[Categories.id], it[Categories.category])
                booksData.add(createBook(
                    it[Books.id],
                    it[Books.title],
                    it[Books.description],
                    it[Books.author],
                    it[Books.price],
                    it[Books.categoryId],
                    category
                ))
            }
        }

        return booksData
    }

    override fun get(id: Int?): MutableList<BookData> {
        if(id != null) {
            return getBook(id)
        }
        return getBooks()
    }

    override fun post(data: BookData): BookData {
        var id: Int = 0

        transaction {
            id = Books.insert {
                it[title] = data.title
                it[description] = data.description
                it[author] = data.author
                it[price] = data.price
                it[categoryId] = data.categoryId
            } get Books.id
        }

        return BookData(id, data.title, data.description, data.author, data.price, data.categoryId, null)
    }

    override fun update(data: BookData): MutableList<BookData> {
        transaction {
            val id: Int = data.id!!
            Books.update({Books.id eq id}) {
                it[Books.title] = data.title
                it[Books.categoryId] = data.categoryId
            }
        }

        return getBook(data.id!!)
    }

    override fun delete(id: Int) {
        transaction {
            Books.deleteWhere { Books.id eq id }
        }
    }
}