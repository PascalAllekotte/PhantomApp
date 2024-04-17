package de.syntax.androidabschluss.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.syntax.androidabschluss.converters.TypeConverter
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.data.model.open.Chat


@Database(
    entities = [Chat::class, Assistant::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(TypeConverter::class)
abstract class ChatGPTDatabase : RoomDatabase() {
    abstract val chatGptDao : ChatGPTDao
    abstract val assistantDao : AssistantDao


    companion object {

        @Volatile
        private var INSTANCE : ChatGPTDatabase? = null

        fun getInstance(context: Context) : ChatGPTDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChatGPTDatabase::class.java,
                    "chat_gpt_db"
                ).build()
                    .also {
                        INSTANCE = it
                    }
            }
        }

    }
}