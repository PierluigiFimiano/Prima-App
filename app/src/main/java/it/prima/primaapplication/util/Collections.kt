package it.prima.primaapplication.util

fun <T> buildList(vararg elements: T, builder: MutableList<T>.() -> Unit): List<T> {
    return mutableListOf(*elements).apply {
        builder.invoke(this)
    }
}