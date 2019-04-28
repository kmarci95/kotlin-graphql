data class BookData(
    val id: Int? = null,
    val title: String,
    val description: String,
    val author: String,
    val price: Int,
    val categoryId: Int,
    val category: CategoryData? = null
) {

}
