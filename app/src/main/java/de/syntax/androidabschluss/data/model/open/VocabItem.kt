package de.syntax.androidabschluss.data.model.open
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VocabItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,       // Das Wort in der Ausgangssprache
    val translation: String // Die Übersetzung des Wortes
)
