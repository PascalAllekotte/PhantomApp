package de.syntax.androidabschluss.data.model.open.Request

data class CompletionRequest(
    val prompt: String,
    val max_tokens: Int
)
