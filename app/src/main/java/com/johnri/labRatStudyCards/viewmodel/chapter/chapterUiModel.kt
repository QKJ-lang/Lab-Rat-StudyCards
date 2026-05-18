package com.johnri.labRatStudyCards.viewmodel.chapter

import androidx.room.Embedded
import com.johnri.labRatStudyCards.data.entity.chapterEntity

// Nos deja contar el número de capítulos sin necesitar de clicar en el capítulo.

data class chapterWithCount(
    @Embedded val chapter: chapterEntity,
    val cardCount: Int
)