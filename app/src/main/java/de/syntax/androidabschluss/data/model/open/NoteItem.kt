package de.syntax.androidabschluss.data.model.open

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Assign a default value to the id
    val title: String,
    val content: String
)
