package de.syntax.androidabschluss.adapter.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import de.syntax.androidabschluss.data.model.open.Chat


@Database(
    entities = [Chat::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(TypeConverter::class)
abstract class GhatGPTDatabase : RoomDatabase() {
    abstract val chatGptDao : ChatGPTDao


    companion object {

        @Volatile
        private var INSTANCE : GhatGPTDatabase? = null

        fun getInstance(context: Context) : GhatGPTDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GhatGPTDatabase::class.java,
                    "chat_gpt_db"
                ).build()
                    .also {
                        INSTANCE = it
                    }
            }
        }

    }
}