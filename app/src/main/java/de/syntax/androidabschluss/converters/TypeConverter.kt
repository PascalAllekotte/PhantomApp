package de.syntax.androidabschluss.converters

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {

    @TypeConverter
    fun fromTimestamp(value : Long) : Date {
        return Date(value)

    }


    @TypeConverter
    fun deleteTimestamp(date : Date) : Long {
        return date.time

    }


}