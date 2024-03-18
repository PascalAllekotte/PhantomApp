package de.syntax.androidabschluss.adapter.local

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {

    @TypeConverter
    fun fromTimestamp(value : String) : Date {
        return Date(value)

    }


    @TypeConverter
    fun deleteTimestamp(date : Date) : Long {
        return date.time

    }


}