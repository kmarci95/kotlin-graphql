data class OrderData(
    val id: Int? = null,
    val userId: Int,
    val bookIds: MutableList<Int>? = null,
    val book: BookData? = null
) {

}
