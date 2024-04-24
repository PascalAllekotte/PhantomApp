package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.data.model.open.Request.CreateImageRequest
import de.syntax.androidabschluss.data.repositorys.ChatRepository
import java.io.File


class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)

    val chatStateFlow get() = chatRepository.chatStateFlow
    val imageStateFlow get() = chatRepository.imageStateFlow

    fun createChatCompletion(message: String, assistantId: String) {
        chatRepository.createChatCompletion(message, assistantId)
    }

    fun createImage(body: CreateImageRequest) {
        chatRepository.createImage(body, getApplication())
    }

    fun getChatList(assistantId: String) {
        chatRepository.getChatList(assistantId)
    }

    fun createImageEdit(
        prompt: String,
        originalImage: File,
        maskImage: File,
        n: Int,
        size: String,
    ) {
        chatRepository.createImageEdit(prompt, originalImage, maskImage, n, size)
    }

}
