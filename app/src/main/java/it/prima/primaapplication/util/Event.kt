package it.prima.primaapplication.util

data class Event<T : Any>(val content: T) {

    var hasBeenHandle = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandle) {
            hasBeenHandle = false
            content
        } else {
            null
        }
    }

    fun peekContent(): T = content

}
