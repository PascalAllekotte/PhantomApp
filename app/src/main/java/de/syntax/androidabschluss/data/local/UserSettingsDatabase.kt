package de.syntax.androidabschluss.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.UserSettings


@Database(entities = [UserSettings::class], version = 1)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: UserSettingsDatabase? = null

        fun getDatabase(context: Context): UserSettingsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserSettingsDatabase::class.java,
                    "user_database"
                ).also { INSTANCE }.build()

                Log.d("DatabaseInitialization", "User Database initialisiert")
                instance
            }.also {
                Log.d("DatabaseInit2", "Datenbankinstanz zurückgegeben")
            }
        }
    }
}