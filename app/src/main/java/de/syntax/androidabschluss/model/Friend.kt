package com.example.random.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Friend(
    @PrimaryKey(autoGenerate = true)
    val friendId: Int?,
    val name: String
)