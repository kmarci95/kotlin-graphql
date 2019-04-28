import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.ArrayList

class CategoryController() {
    public fun getCategories(): MutableList<CategoryData> {
        val categoriesData: MutableList<CategoryData> = ArrayList()

        transaction {
            Categories.selectAll().forEach {
                categoriesData.add(CategoryData(it[Categories.id], it[Categories.category]))
            }
        }

        return categoriesData;
    }

    public fun postCategory(categoryString: String): CategoryData {
        var id: Int = 0

        transaction {
            id = Categories.insert { it[category] = categoryString } get Categories.id
        }

        return CategoryData(id, categoryString)
    }
}