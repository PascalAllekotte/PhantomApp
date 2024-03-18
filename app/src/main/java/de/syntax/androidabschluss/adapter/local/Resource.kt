package de.syntax.androidabschluss.adapter.local

import de.syntax.androidabschluss.utils.Status

sealed class Resource<T>(val status: Status, val data: T? = null, val message : String? = null) {

    class Success<T>(data: T?= null,message: String? = "") : Resource<T>(Status.SUCCESS,data,message)
    class Error<T>(message: String,data: T? = null) : Resource<T>(Status.ERROR,data,message)
    class Loading<T> : Resource<T>(Status.LOADING)

}