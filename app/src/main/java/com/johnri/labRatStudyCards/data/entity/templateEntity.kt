package com.johnri.labRatStudyCards.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class templateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val frontTemplate: String,
    val backTemplate: String
)