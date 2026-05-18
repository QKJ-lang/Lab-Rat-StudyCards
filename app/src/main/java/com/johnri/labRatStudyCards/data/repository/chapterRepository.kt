package com.johnri.labRatStudyCards.data.repository

import androidx.lifecycle.LiveData
import com.johnri.labRatStudyCards.data.dao.chapterDao
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.viewmodel.chapter.chapterWithCount
import kotlinx.coroutines.flow.Flow


class chapterRepository(private val chapterDao: chapterDao) {

    suspend fun createChapter(
        deckId: Int,
        name: String
    ) {

        chapterDao.insertChapter(
            chapterEntity(
                name = name,
                deckId = deckId
            )
        )
    }

    suspend fun insertChapter(chapter: chapterEntity) {
        chapterDao.insertChapter(chapter)
    }

    suspend fun deleteChapter(chapter: chapterEntity) {
        chapterDao.deleteChapter(chapter)
    }

    suspend fun insertChapterAndReturnId(chapter: chapterEntity): Int {
        return chapterDao.insert(chapter).toInt()
    }

    fun getAllChapters(): Flow<List<chapterEntity>> {
        return chapterDao.getAllChapters()
    }

    fun getChaptersWithCardCount(deckId: Int) =
        chapterDao.getChaptersWithCardCount(deckId)

    fun getChapters(
        deckId: Int
    ): LiveData<List<chapterWithCount>> {

        return chapterDao.getChaptersWithCount(deckId)
    }

}