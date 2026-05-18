package com.johnri.labRatStudyCards.data.repository

import com.johnri.labRatStudyCards.data.dao.statsDao
import com.johnri.labRatStudyCards.data.entity.statsEntity
import kotlinx.coroutines.flow.Flow

class statsRepository(private val statsDao: statsDao) {

    fun getAllStats(): Flow<List<statsEntity>> {
        return statsDao.getAllStats()
    }

    suspend fun getTodayStats(date: Long): statsEntity? {
        return statsDao.getStatsByDate(date)
    }

    suspend fun insertOrUpdate(stats: statsEntity) {
        val existing = statsDao.getStatsByDate(stats.date)
        if (existing == null) {
            statsDao.insertStats(stats)
        } else {
            statsDao.updateStats(stats)
        }
    }
}