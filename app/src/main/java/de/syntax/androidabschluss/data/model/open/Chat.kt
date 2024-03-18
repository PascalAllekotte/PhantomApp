package de.syntax.androidabschluss.data.model.open

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.syntax.androidabschluss.response.Message
import java.util.Date


@Entity
data class Chat(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("chatId")
    val chatId : String,
    @Embedded
    val message : Message,
    val date : Date

)
