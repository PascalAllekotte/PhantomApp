package de.syntax.androidabschluss.data.model.open

import java.util.Date

data class Chat(
    val chatId : String,
    val message : String,
    val messageType : String,
    val date : Date

)
