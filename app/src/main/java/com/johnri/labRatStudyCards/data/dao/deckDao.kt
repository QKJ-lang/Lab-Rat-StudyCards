package com.johnri.labRatStudyCards.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.johnri.labRatStudyCards.data.entity.deckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface deckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: deckEntity): Long

    @Query("SELECT * FROM decks WHERE userId = :userId")
    fun getDecksByUser(userId: Int): Flow<List<deckEntity>>

    @Delete
    suspend fun deleteDeck(deck: deckEntity)

    @Query("DELETE FROM decks WHERE userId = :userId")
    suspend fun deleteAllDecksByUser(userId: Int)

    @Query("""
UPDATE decks
SET name = :title,
    color = :color
WHERE id = :deckId
""")
    suspend fun updateDeck(
        deckId: Int,
        title: String,
        color: String
    )
}