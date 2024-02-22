package de.syntax.androidabschluss.model

data class CompletionRequest(
    val prompt: String,
    val max_tokens: Int
)
