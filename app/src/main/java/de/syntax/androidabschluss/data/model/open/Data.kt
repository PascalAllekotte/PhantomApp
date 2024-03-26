package de.syntax.androidabschluss.data.model.open

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val revised_prompt :String,
    val url: String
)