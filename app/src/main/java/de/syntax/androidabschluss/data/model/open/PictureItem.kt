package de.syntax.androidabschluss.data.model.open

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures4")
data class PictureItem(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val url: String,
        val created: Int
    )

