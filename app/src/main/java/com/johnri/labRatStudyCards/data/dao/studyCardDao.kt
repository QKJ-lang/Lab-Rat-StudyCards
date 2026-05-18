package com.johnri.labRatStudyCards.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface studyCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: studyCardEntity)

    @Query("SELECT * FROM study_cards WHERE nextReview <= :today LIMIT 30")
    suspend fun getCardsForToday(today: Long): List<studyCardEntity>

    @Query("SELECT * FROM study_cards WHERE chapterId = :chapterId")
    fun getCardsByChapter(chapterId: Int): Flow<List<studyCardEntity>>

    @Query("SELECT * FROM study_cards WHERE chapterId = :chapterId")
    suspend fun getCardsFromChapter(chapterId: Int): List<studyCardEntity>

    @Query("SELECT * FROM study_cards ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCards(limit: Int): List<studyCardEntity>

    @Query("""SELECT sc.* FROM study_cards sc INNER JOIN chapters c ON sc.chapterId = c.id WHERE c.deckId = :deckId ORDER BY RANDOM() LIMIT 30""")
    suspend fun getRandomCardsFromDeck(deckId: Int): List<studyCardEntity>

    @Query("SELECT COUNT(*) FROM study_cards WHERE nextReview <= :now")
    suspend fun getPendingCards(now: Long): Int

    @Update
    suspend fun updateCard(card: studyCardEntity)

    @Insert
    suspend fun insert(card: studyCardEntity)

    @Query("SELECT COUNT(*) FROM study_cards WHERE lastReviewed >= :startOfDay")
    suspend fun getCardsReviewedToday(startOfDay: Long): Int

    @Query("SELECT COUNT(*) FROM study_cards WHERE nextReview <= :now")
    suspend fun getPendingToday(now: Long): Int

    @Query("SELECT COUNT(*) FROM study_cards WHERE lastReviewed >= :startOfWeek")
    suspend fun getCardsReviewedThisWeek(startOfWeek: Long): Int
}