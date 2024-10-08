package de.syntax.androidabschluss.data.remote

import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.seconds

class ApiBuilder {

    private val apikey = "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"
    private val config = OpenAIConfig(token = apikey, timeout = Timeout(60.seconds))
    private val openAiApi = OpenAI(config)


    fun senMessageRequestToApi(messages: String): Flow<ChatCompletionChunk> {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = messages
                )
            )
        )
    return openAiApi.chatCompletions(chatCompletionRequest)

    }


}