package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.data.repositorys.ChatRepository
import de.syntax.androidabschluss.response.CreateImageRequest

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)

    val chatStateFlow get() = chatRepository.chatStateFlow
    val imageStateFlow get() = chatRepository.imageStateFlow

    fun createChatCompletion(message: String, robotId: String) {
        chatRepository.createChatCompletion(message, robotId)
    }

    fun createImage(body: CreateImageRequest) {
        chatRepository.createImage(body)
    }

    fun getChatList(robotId: String) {
        chatRepository.getChatList(robotId)
    }


}
