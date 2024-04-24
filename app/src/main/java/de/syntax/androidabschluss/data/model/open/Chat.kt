package de.syntax.androidabschluss.data.model.open

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.syntax.androidabschluss.data.model.open.response.Message
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Assistant::class,
        parentColumns = arrayOf("assistantId"),
        childColumns = arrayOf("assistantId"),
        onDelete = ForeignKey.CASCADE

    )]
)
data class Chat(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chatId")
    val chatId: String,
    @Embedded
    val message: Message,
    val assistantId: String,
    val date: Date
)
