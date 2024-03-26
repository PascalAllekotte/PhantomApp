package de.syntax.androidabschluss.data.model.open

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "url")
    val url: String
)