package de.syntax.androidabschluss.response

data class CreateImageRequest(
    val model: String,
    val n: Int,
    val prompt: String,
    val size: String
)