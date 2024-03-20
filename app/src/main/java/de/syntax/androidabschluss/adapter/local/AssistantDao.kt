package de.syntax.androidabschluss.adapter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.Assistant
import kotlinx.coroutines.flow.Flow


@Dao
interface AssistantDao {

    @Query("SELECT * FROM Assistant")
    fun getAssistantList() : Flow<List<Assistant>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistant(assistant : Assistant) : Long

    @Update()
    suspend fun updateAssistant(assistant : Assistant) : Int

    @Query("DELETE FROM Assistant WHERE assistantId == :assistantId ")
    fun deleteAssistantUsingId(assistantId : String) : Int

    @Query("DELETE FROM Chat WHERE assistantId == :assistantId ")
    fun deleteChatUsingAssistantId(assistantId : String)


}