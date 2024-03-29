package com.example.datebook.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM localDataBase")
    suspend fun readEvents(): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheEvents(vararg events: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvents(vararg events: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}
