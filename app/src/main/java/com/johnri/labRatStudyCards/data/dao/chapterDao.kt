package com.johnri.labRatStudyCards.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.viewmodel.chapter.chapterWithCount
import kotlinx.coroutines.flow.Flow


@Dao
interface chapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: chapterEntity)

    @Query("SELECT * FROM chapters WHERE deckId = :deckId")
    fun getChaptersByDeck(deckId: Int): Flow<List<chapterEntity>>

    @Delete
    suspend fun deleteChapter(chapter: chapterEntity)

    @Insert
    suspend fun insert(chapter: chapterEntity): Long

    @Query("SELECT * FROM chapters WHERE deckId = :deckId")
    fun getChapters(deckId: Int): Flow<List<chapterEntity>>

    @Query("SELECT * FROM chapters")
    fun getAllChapters(): Flow<List<chapterEntity>>

    @Query("""
        SELECT c.*, COUNT(s.id) AS cardCount
        FROM chapters c
        LEFT JOIN study_cards s ON s.chapterId = c.id
        WHERE c.deckId = :deckId
        GROUP BY c.id
    """)
    fun getChaptersWithCardCount(deckId: Int): Flow<List<chapterWithCount>>

    @Query("""
    SELECT c.*, COUNT(s.id) AS cardCount
    FROM chapters c
    LEFT JOIN study_cards s
        ON s.chapterId = c.id
    WHERE c.deckId = :deckId
    GROUP BY c.id
""")
    fun getChaptersWithCount(
        deckId: Int
    ): LiveData<List<chapterWithCount>>
}