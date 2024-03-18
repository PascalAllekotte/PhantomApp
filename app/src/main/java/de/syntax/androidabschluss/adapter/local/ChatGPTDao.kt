package de.syntax.androidabschluss.adapter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.syntax.androidabschluss.data.model.open.Chat
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ChatGPTDao {

    @Query("SELECT * FROM Chat ORDER BY date DESC")
    fun getChatList() : Flow<List<Chat>>

    @Query("SELECT * FROM Chat ORDER BY date DESC LIMIT 5")
    fun getChatListFlow() : List<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat : Chat) : Long

    @Query("DELETE FROM Chat WHERE chatId == :chatId ")
    fun deleteChatUsingChatId(chatId : String) : Int

    @Query("UPDATE CHAT SET content =:content, role=:role, date=:date WHERE chatId == :chatId")
    suspend fun updateChatPaticularField(
        chatId: String,
        content: String,
        role: String,
        date: Date
    )




}