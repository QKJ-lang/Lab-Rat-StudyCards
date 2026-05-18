package com.johnri.labRatStudyCards.data.repository

import com.johnri.labRatStudyCards.data.dao.deckDao
import com.johnri.labRatStudyCards.data.entity.deckEntity
import kotlinx.coroutines.flow.Flow


class deckRepository(private val deckDao: deckDao) {

    fun getDecksByUser(userId: Int): Flow<List<deckEntity>> {
        return deckDao.getDecksByUser(userId)
    }

    suspend fun insertDeck(deck: deckEntity) {
        deckDao.insertDeck(deck)
    }

    suspend fun deleteDeck(deck: deckEntity) {
        deckDao.deleteDeck(deck)
    }

    suspend fun insertDeckAndReturnId(deck: deckEntity): Int {
        return deckDao.insertDeck(deck).toInt()
    }
    suspend fun deleteAllDecksByUser(userId: Int) {
        deckDao.deleteAllDecksByUser(userId)
    }
    suspend fun updateDeck(
        deckId: Int,
        title: String,
        color: String
    ) {
        deckDao.updateDeck(deckId, title, color)
    }
}