package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.data.model.open.Chat

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    var chatList = MutableLiveData<List<Chat>>(arrayListOf())
        private set

        fun insertChat(chat: Chat){
            val modifiedChatList = ArrayList<Chat>().apply {
                addAll(chatList.value!!)
            }
            modifiedChatList.add(chat)

            chatList.postValue(modifiedChatList)

        }

}