package com.johnri.labRatStudyCards.viewmodel.addCardViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johnri.labRatStudyCards.data.repository.studyCardRepository

class addCardViewModelFactory(

    private val repository: studyCardRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(addCardViewModel::class.java)) {
            return addCardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}