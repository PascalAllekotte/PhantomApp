package de.syntax.androidabschluss.data.model.open

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.syntax.androidabschluss.response.Message

@Entity()
data class Assistant(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("assistantId")
    val assistantId : String,
    val assistantName : Message,
    val assistantImg : Int
)
