package de.syntax.androidabschluss.adapter.local

import androidx.room.Database
import androidx.room.TypeConverter
import de.syntax.androidabschluss.data.model.open.Chat


@Database(
    entities = [Chat::class],
    version = 1,
    exportSchema = false
)@TypeConverter(TypeConverter::class)
class GhatGPTDatabase {
}