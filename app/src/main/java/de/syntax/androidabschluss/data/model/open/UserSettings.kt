package de.syntax.androidabschluss.data.model.open

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val color: Int
    )
