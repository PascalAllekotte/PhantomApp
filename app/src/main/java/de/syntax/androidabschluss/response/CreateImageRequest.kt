package de.syntax.androidabschluss.response

data class CreateImageRequest(
    val n: Int,
    val prompt: String,
    val size: String
)