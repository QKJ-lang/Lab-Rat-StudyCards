package com.johnri.labRatStudyCards.viewmodel.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johnri.labRatStudyCards.data.repository.chapterRepository

class chapterViewModelFactory(
    private val repository: chapterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(chapterViewModel::class.java)) {
            return chapterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}