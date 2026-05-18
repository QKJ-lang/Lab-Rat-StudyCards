package com.johnri.labRatStudyCards.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_cards",
    foreignKeys = [
        ForeignKey(
            entity = chapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterId")]
)
data class studyCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val question: String,
    val answer: String,

    val chapterId: Int? = null,

    // SRS fields
    val intervals: Int = 1,            // days
    val easiness: Double = 2.5,     // difficulty
    val repetitions: Int = 0,
    val nextReview: Long,             // timestamp
    val lastReviewed: Long = 0,
    val lastQuality: Int = -1
)