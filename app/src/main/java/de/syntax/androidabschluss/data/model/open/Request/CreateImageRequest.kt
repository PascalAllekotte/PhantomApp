package de.syntax.androidabschluss.data.model.open.Request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateImageRequest(
    @Json(name = "n")
    val n: Int,
    @Json(name = "quality")
    val quality: String,
    @Json(name = "prompt")
    val prompt: String,
    @Json(name = "size")
    val size: String,
    @Json(name = "style")
    val style: String,
    @Json(name = "model")
    val model: String
)
