package com.johnri.labRatStudyCards.data.dao

import androidx.room.Dao
import androidx.room.Update
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.johnri.labRatStudyCards.data.entity.statsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface statsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: statsEntity)

    @Query("SELECT * FROM stats WHERE date = :date LIMIT 1")
    suspend fun getStatsByDate(date: Long): statsEntity?

    @Query("SELECT * FROM stats ORDER BY date DESC")
    fun getAllStats(): Flow<List<statsEntity>>

    @Update
    suspend fun updateStats(stats: statsEntity)
}