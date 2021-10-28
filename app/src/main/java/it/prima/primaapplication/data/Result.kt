package it.prima.primaapplication.data

import com.apollographql.apollo.api.Response

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

inline fun <T> apolloResponse(block: () -> Response<T>): Result<T> {
    return try {
        val response = block()
        if (response.data != null && !response.hasErrors()) {
            return Result.Success(response.data!!)
        }
        Result.Error(Exception())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
