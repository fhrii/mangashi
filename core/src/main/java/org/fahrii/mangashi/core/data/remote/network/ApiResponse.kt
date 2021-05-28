package org.fahrii.mangashi.core.data.remote.network

sealed class ApiResponse<out T> {
    data class SUCCESS<out R>(val data: R) : ApiResponse<R>()
    data class ERROR(val errMessage: String) : ApiResponse<Nothing>()
    data class EMPTY<out R>(val data: R) : ApiResponse<R>()
}
