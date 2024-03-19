package de.syntax.androidabschluss.adapter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import de.syntax.androidabschluss.data.model.open.Assistant


@Dao
interface AssistantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistant(robot : Assistant) : Long
}