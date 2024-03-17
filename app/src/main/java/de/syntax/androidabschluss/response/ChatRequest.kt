package de.syntax.androidabschluss.response

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)