package com.johnri.labRatStudyCards.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stats")
data class statsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: Long,
    val cardsReviewed: Int,
    val correctAnswers: Int
)