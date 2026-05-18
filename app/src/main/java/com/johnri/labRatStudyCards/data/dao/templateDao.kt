package com.johnri.labRatStudyCards.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.johnri.labRatStudyCards.data.entity.templateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface templateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: templateEntity)

    @Query("SELECT * FROM templates")
    fun getAllTemplates(): Flow<List<templateEntity>>

    @Query("SELECT * FROM templates WHERE id = :id LIMIT 1")
    suspend fun getTemplateById(id: Int): templateEntity?

    @Delete
    suspend fun deleteTemplate(template: templateEntity)
}