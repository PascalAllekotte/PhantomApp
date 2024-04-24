package de.syntax.androidabschluss.converters

import androidx.room.TypeConverter
import java.util.Date

// Konverter-Klasse für Room-Datenbankoperationen zur Umwandlung zwischen Datum und Timestamp
class TypeConverter {

    // Konvertiert einen Long-Wert (Timestamp) in ein Date-Objekt
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    // Konvertiert ein Date-Objekt in einen Long-Wert (Timestamp)
    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}
