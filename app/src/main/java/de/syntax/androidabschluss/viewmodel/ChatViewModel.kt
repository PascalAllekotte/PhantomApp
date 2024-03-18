package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.data.repositorys.ChatRepository

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)
    val chatStateFlow get() = chatRepository.chatStateFlow



    fun createChatCompletion(message: String){
        chatRepository.createChatCompletion(message)
    }


    fun getChatList(){
        chatRepository.getChatList()
    }

}
