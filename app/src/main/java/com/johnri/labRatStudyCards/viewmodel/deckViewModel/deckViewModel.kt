package com.johnri.labRatStudyCards.viewmodel.deckViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnri.labRatStudyCards.data.repository.deckRepository
import kotlinx.coroutines.launch

class deckViewModel(
    private val repository: deckRepository
) : ViewModel() {

    fun updateDeck(
        deckId: Int,
        title: String,
        color: String
    ) {
        viewModelScope.launch {
            repository.updateDeck(
                deckId,
                title,
                color
            )
        }
    }
}