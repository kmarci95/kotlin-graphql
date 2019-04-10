data class BookData(
    val id: Int,
    val title: String,
    val categoryId: Int,
    val category: CategoryData? = null
) {

}
