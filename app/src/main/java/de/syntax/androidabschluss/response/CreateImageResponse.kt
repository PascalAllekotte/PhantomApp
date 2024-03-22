package de.syntax.androidabschluss.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateImageResponse(
    val created: Int,
    @Json(name = "data")
    val data: List<Data>
)

@JsonClass(generateAdapter = true)
data class Data(
    val url: String
)
