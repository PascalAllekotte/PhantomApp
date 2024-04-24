package de.syntax.androidabschluss.data.model.open.Request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.syntax.androidabschluss.data.model.open.response.Message

@JsonClass(generateAdapter = true)
data class ChatRequest(
    @Json(name = "messages") val messages: List<Message>,
    @Json(name = "model") val model: String
)