package de.syntax.androidabschluss.response

import com.google.gson.annotations.SerializedName

data class CreateImageResponse(
    val created: Int,
    @SerializedName("data")
    val data: List<Data>
)

data class Data(
    val url: String
)