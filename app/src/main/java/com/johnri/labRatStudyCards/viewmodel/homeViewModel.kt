package com.johnri.labRatStudyCards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.johnri.labRatStudyCards.data.entity.deckEntity
import com.johnri.labRatStudyCards.data.repository.deckRepository

class homeViewModel(
    private val deckRepository: deckRepository
) : ViewModel() {

    private val _userId = MutableLiveData<Int>()

    val decks: LiveData<List<deckEntity>> = _userId.switchMap { userId ->
        deckRepository.getDecksByUser(userId).asLiveData()
    }

    fun setUser(userId: Int) {
        _userId.value = userId
    }
}