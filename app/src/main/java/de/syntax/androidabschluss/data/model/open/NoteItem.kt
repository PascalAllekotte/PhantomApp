package de.syntax.androidabschluss.data.model.open

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "")
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    var dateTime: String? = null,
)
