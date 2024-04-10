package de.syntax.androidabschluss.data.model.open

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class TermItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val termText: String,
    val termDate: String

)