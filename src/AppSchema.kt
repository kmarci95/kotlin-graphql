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

        query("categories") {
            resolver { -> categoryController.getCategories() }
        }

        mutation("createCategory") {
            description = "Creates a category"
            resolver { category: String -> categoryController.postCategory(category) }
        }

        mutation("createBook") {
            description = "Creates book with attributes"
            resolver { title: String, categoryId: Int -> booksController.postBook(title, categoryId) }
        }

        mutation("updateBook") {
            description = "Updates Book"
            resolver { id: Int, title: String, categoryId: Int -> booksController.update(id, title, categoryId) }
        }

        type<BookData>()
        type<CategoryData>()
    }
}
