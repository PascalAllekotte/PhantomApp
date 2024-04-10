package de.syntax.androidabschluss.adapter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.UserSettings

@Dao
interface UserSettingsDao{

    @Insert
    suspend fun insertAllSettings(settings: UserSettings)

    @Query("SELECT * FROM UserSettings")
    suspend fun getSettings(): UserSettings?

    @Update
    suspend fun updateSettings(settings: UserSettings)

}