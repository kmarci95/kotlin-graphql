import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.print.Book
import java.util.ArrayList

class BooksController() {
    public fun getBooks(): MutableList<BookData> {
        val booksData: MutableList<BookData> = ArrayList()

        transaction {
            (Books innerJoin Categories).selectAll().forEach {
                val category = CategoryData(it[Categories.id], it[Categories.category])
                booksData.add(BookData(
                    it[Books.id],
                    it[Books.title],
                    it[Books.categoryId],
                    category
                ))
            }
        }
        print(booksData)
        return booksData;
    }

    public fun getBook(id: Int): MutableList<BookData> {
        val booksData: MutableList<BookData> = ArrayList()

        transaction {
            (Books innerJoin Categories).select{Books.id eq id}.forEach {
                val category = CategoryData(it[Categories.id], it[Categories.category])
                booksData.add(BookData(
                    it[Books.id],
                    it[Books.title],
                    it[Books.categoryId],
                    category
                ))
            }
        }

        return booksData
    }

    public fun postBook(bookTitle: String, bookCategoryId: Int): BookData {
        var id: Int = 0

        transaction {
            id = Books.insert {
                it[title] = bookTitle
                it[categoryId] = bookCategoryId
            } get Books.id
        }

        return BookData(id, bookTitle, bookCategoryId, null)
    }

    public fun update(id: Int, title: String, categoryId: Int): MutableList<BookData> {
        transaction {
            Books.update({Books.id eq id}) {
                it[Books.title] = title
                it[Books.categoryId] = categoryId
            }
        }

        return getBook(id)
    }

    public fun deleteBook(id: Int) {
        transaction {
            Books.deleteWhere { Books.id eq id }
        }
    }
}