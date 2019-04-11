import com.github.pgutkowski.kgraphql.KGraphQL
import java.time.LocalDate

class AppSchema(
    val booksController: BooksController,
    val categoryController: CategoryController
) {

    public val schema = KGraphQL.schema {

        configure {
            useDefaultPrettyPrinter = true
        }

        query("books") {
            resolver { -> booksController.getBooks() }
        }

        query("book") {
            resolver { id: Int -> booksController.getBook(id) }
        }

        mutation("createBook") {
            description = "Creates book with attributes"
            resolver { title: String, categoryId: Int ->
                val book = BookData(null, title, categoryId, null)
                booksController.post(book)
             }
        }

        mutation("updateBook") {
            description = "Updates Book"
            resolver { id: Int, title: String, categoryId: Int ->
                val book = BookData(id, title, categoryId, null)
                booksController.update(book)
            }
        }

        query("categories") {
            resolver { -> categoryController.getCategories() }
        }

        mutation("createCategory") {
            description = "Creates a category"
            resolver { category: String -> categoryController.postCategory(category) }
        }

        type<BookData>()
        type<CategoryData>()
    }
}
