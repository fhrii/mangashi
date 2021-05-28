package org.fahrii.mangashi.core.utils

interface ResponseMapper<Response, Model> {
    fun mapFromResponse(response: Response): Model
}