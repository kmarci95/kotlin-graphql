package com.rest

import org.jetbrains.exposed.sql.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import java.text.DateFormat
import io.ktor.request.*
import io.ktor.response.*
import BooksController
import CategoryController
import AppSchema

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    Database.connect(
        "jdbc:postgresql://localhost:5432/bookshop",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "docker"
    )

    val booksController = BooksController()
    val categoryController = CategoryController()
    val appSchema = AppSchema(booksController, categoryController)

    routing {
        post("/graphql") {
            val request = call.receive<GraphQLRequest>()
            val query = request.query

            call.respond(appSchema.schema.execute(query))
        }

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/books") {
            call.respond(booksController.getBooks())
        }

        get("/books/{id}") {
            val id = call.parameters["id"]!!.toInt()
            call.respond(booksController.getBook(id))
        }

        post("/books") {
            val postParameters: Parameters = call.receiveParameters()
            val bookTitle: String = postParameters["title"] as String
            val bookCategoryId: Int = postParameters["categoryId"] as Int

            call.respond(booksController.postBook(bookTitle, bookCategoryId))
        }

        delete("/books/{id}") {
            val id = call.parameters["id"]!!.toInt()
            booksController.deleteBook(id)
            call.response.status(HttpStatusCode.OK)
        }
    }
}

data class GraphQLRequest(val query: String = "")
