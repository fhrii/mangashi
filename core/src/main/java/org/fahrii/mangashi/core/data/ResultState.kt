package org.fahrii.mangashi.core.data

sealed class ResultState<out T> {
    data class SUCCESS<out R>(val data: R) : ResultState<R>()
    data class ERROR(val errMessage: String? = null) : ResultState<Nothing>()
    object LOADING : ResultState<Nothing>()
}