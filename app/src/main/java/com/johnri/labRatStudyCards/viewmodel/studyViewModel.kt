package com.johnri.labRatStudyCards.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnri.labRatStudyCards.data.algorithm.spacedRepAlg
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import kotlinx.coroutines.launch

class studyViewModel(
    private val repository: studyCardRepository
) : ViewModel() {

    val cards = MutableLiveData<List<studyCardEntity>>()

    private var currentIndex = 0
    private var currentList: List<studyCardEntity> = emptyList()

    fun loadCardsFromChapter(chapterId: Int) {
        viewModelScope.launch {
            currentList = repository.getCardsFromChapter(chapterId)
            cards.value = currentList
        }
    }

    fun loadRandomCards(limit: Int) {
        viewModelScope.launch {
            currentList = repository.getRandomCards(limit)
            currentIndex = 0
            cards.value = currentList
        }
    }

    fun answerCard(quality: Int) {
        val currentCard = currentList[currentIndex]

        val updatedCard = spacedRepAlg.apply(currentCard, quality).copy( lastReviewed = System.currentTimeMillis(), lastQuality = quality)
        viewModelScope.launch {
            repository.updateCard(updatedCard)
        }

        currentIndex++

        if (currentIndex < currentList.size) {
            cards.value = listOf(currentList[currentIndex])
        } else {
            cards.value = emptyList() // fin sesión
        }
    }
}