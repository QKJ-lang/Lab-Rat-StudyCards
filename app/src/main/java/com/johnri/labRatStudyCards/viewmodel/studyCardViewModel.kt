package com.johnri.labRatStudyCards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.johnri.labRatStudyCards.data.repository.studyCardRepository

class studyCardViewModel(
    private val repository: studyCardRepository
) : ViewModel() {

    fun getCardsByChapter(chapterId: Int) =
        repository.getCardsByChapter(chapterId).asLiveData()
}