package tech.ctawave.exoplayercmcd.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    /**
     * Status of a resource that is provided to the UI.
     */
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

}
