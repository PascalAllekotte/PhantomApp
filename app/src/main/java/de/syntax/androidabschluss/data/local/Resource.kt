package de.syntax.androidabschluss.data.local

import de.syntax.androidabschluss.utils.Status

// Abstrakte Basis-Klasse zur Repräsentation des Zustands einer Datenabfrage
sealed class Resource<T>(val status: Status, val data: T? = null, val message: String? = null) {

    // Repräsentiert einen erfolgreichen Datenabruf
    class Success<T>(data: T? = null, message: String? = "") : Resource<T>(Status.SUCCESS, data, message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(Status.ERROR, data, message)
    class Loading<T> : Resource<T>(Status.LOADING)
}
