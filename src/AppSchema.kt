import com.github.pgutkowski.kgraphql.KGraphQL
import java.time.LocalDate

class AppSchema(
    val booksController: BooksController,
    val categoryController: CategoryController,
    val usersController: UsersController,
    val ordersController: OrdersController
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
            resolver { title: String, description: String, author: String, price: Int, categoryId: Int ->
                booksController.post(booksController.createBook(
                    null,
                    title,
                    description,
                    author,
                    price,
                    categoryId,
                    null
                ))
             }
        }

        mutation("updateBook") {
            description = "Updates Book"
            resolver { id: Int, title: String, description: String, author: String, price: Int, categoryId: Int ->
                booksController.update(booksController.createBook(
                    id,
                    title,
                    description,
                    author,
                    price,
                    categoryId,
                    null
                ))
            }
        }

        query("categories") {
            resolver { -> categoryController.getCategories() }
        }

        mutation("createCategory") {
            description = "Creates a category"
            resolver { category: String -> categoryController.postCategory(category) }
        }

        query("users") {
            resolver { -> usersController.getUsers()}
        }

        query("user") {
            resolver { id: Int -> usersController.get(id) }
        }

        mutation("registerUser") {
            description = "Register user with attributes"
            resolver { firstName: String, lastName: String, email: String ->
                usersController.post(usersController.createUser(
                    null,
                    firstName,
                    lastName,
                    email
                ))
            }
        }

        query("orders") {
            resolver { -> ordersController.getOrders()}
        }

        query("order") {
            resolver { id: Int -> ordersController.get(id) }
        }

        query("userOrders") {
            resolver { id: Int -> ordersController.getUserOrders(id) }
        }

        mutation("createOrder") {
            description = "Order books"
            resolver { userId: Int, bookIds: MutableList<Int> ->
                ordersController.post(ordersController.create(
                    null,
                    userId,
                    bookIds,
                    null
                ))
            }
        }


        type<BookData>()
        type<CategoryData>()
        type<UserData>()
    }
}
