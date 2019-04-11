abstract class Controller<T>() {
    abstract fun get(id: Int?): MutableList<T>

    abstract fun post(data: T): T

    abstract fun update(data: T): MutableList<T>

    abstract fun delete(id: Int)
}