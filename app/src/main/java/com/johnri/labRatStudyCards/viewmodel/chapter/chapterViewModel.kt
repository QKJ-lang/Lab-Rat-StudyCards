package com.johnri.labRatStudyCards.viewmodel.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.data.repository.chapterRepository
import kotlinx.coroutines.launch

class chapterViewModel(
    private val repository: chapterRepository
) : ViewModel() {

    fun getChapters(deckId: Int) =
        repository.getChapters(deckId)

    fun createChapter(deckId: Int, name: String) {
        viewModelScope.launch {
            repository.createChapter(deckId, name)
        }
    }
    fun deleteChapter(chapter: chapterEntity) {

        viewModelScope.launch {

            repository.deleteChapter(chapter)
        }
    }
}