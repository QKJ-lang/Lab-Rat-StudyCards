package com.johnri.labRatStudyCards.data.repository

import com.johnri.labRatStudyCards.data.dao.studyCardDao
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import kotlinx.coroutines.flow.Flow

class studyCardRepository(
    private val studyCardDao: studyCardDao
) {

    suspend fun insertCard(card: studyCardEntity) {
        studyCardDao.insertCard(card)
    }

    fun getCardsByChapter(chapterId: Int): Flow<List<studyCardEntity>> {
        return studyCardDao.getCardsByChapter(chapterId)
    }

    suspend fun getTodayCards(): List<studyCardEntity> {
        val today = System.currentTimeMillis()
        return studyCardDao.getCardsForToday(today)
    }

    suspend fun getCardsFromChapter(chapterId: Int): List<studyCardEntity> {
        return studyCardDao.getCardsFromChapter(chapterId)
    }

    suspend fun updateCard(card: studyCardEntity) {
        studyCardDao.updateCard(card)
    }

    suspend fun getRandomCards(limit: Int): List<studyCardEntity> {
        return studyCardDao.getRandomCards(limit)
    }

    suspend fun getReviewedToday(start: Long) =
        studyCardDao.getCardsReviewedToday(start)

    suspend fun getPendingToday(now: Long) =
        studyCardDao.getPendingToday(now)

    suspend fun getReviewedWeek(start: Long) =
        studyCardDao.getCardsReviewedThisWeek(start)
}