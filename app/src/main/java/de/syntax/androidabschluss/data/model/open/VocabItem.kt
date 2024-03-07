package de.syntax.androidabschluss.data.model.open
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VocabItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val language: String,
    val language2: String,
    val translation: String,
    val translation2: String,
    val favorite: Boolean
)
