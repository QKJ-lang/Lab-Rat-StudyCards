package com.johnri.labRatStudyCards.viewmodel.deckViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johnri.labRatStudyCards.data.repository.deckRepository

class deckViewModelFactory(
    private val repository: deckRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(deckViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return deckViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}