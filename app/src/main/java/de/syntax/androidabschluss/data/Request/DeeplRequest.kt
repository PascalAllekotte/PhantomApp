package de.syntax.androidabschluss.data.Request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DeeplRequest(
    @Json(name = "target_lang")  val target_lang: String,
    @Json(name = "text") val text: List<String>
)