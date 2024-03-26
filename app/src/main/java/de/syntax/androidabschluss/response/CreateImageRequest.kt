package de.syntax.androidabschluss.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateImageRequest(
    val n: Int,
    val prompt: String,
    val size: String,
    val model:String = "dall-e-3"
)
