package com.example.datebook.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localDataBase")
data class EventEntity(
    @PrimaryKey val id: String,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String,
)
