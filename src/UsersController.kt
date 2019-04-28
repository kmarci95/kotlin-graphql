import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.print.Book
import java.util.ArrayList

class UsersController() : Controller<UserData>() {

    fun createUser(id: Int?, firstName: String, lastName: String, email: String): UserData {
        return UserData(id, firstName, lastName, email);
    }

    fun getUsers(): MutableList<UserData> {
        val usersData: MutableList<UserData> = ArrayList()

        transaction {
            Users.selectAll().forEach {
                usersData.add(createUser(
                    it[Users.id],
                    it[Users.firstName],
                    it[Users.lastName],
                    it[Users.email]
                ))
            }
        }

        return usersData
    }

    fun getUser(id: Int): MutableList<UserData> {
        val usersData: MutableList<UserData> = ArrayList()

        transaction {
            Users.select{Users.id eq id}.forEach {
                usersData.add(createUser(
                    it[Users.id],
                    it[Users.firstName],
                    it[Users.lastName],
                    it[Users.email]
                ))
            }
        }

        return usersData
    }

    override fun get(id: Int?): MutableList<UserData> {
        if(id != null) {
            return getUser(id)
        }
        return getUsers()
    }

    override fun post(data: UserData): UserData {
        var id: Int = 0

        transaction {
            id = Users.insert {
                it[firstName] = data.firstName
                it[lastName] = data.lastName
                it[email] = data.email
            } get Users.id
        }

        return UserData(id, data.firstName, data.lastName, data.email)
    }

    override fun update(data: UserData): MutableList<UserData> {
        transaction {
            val id: Int = data.id!!
            Users.update({Users.id eq id}) {
                it[Users.firstName] = data.firstName
                it[Users.lastName] = data.lastName
                it[Users.email] = data.email
            }
        }

        return getUser(data.id!!)
    }

    override fun delete(id: Int) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}