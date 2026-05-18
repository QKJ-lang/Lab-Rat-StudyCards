package com.johnri.labRatStudyCards.viewmodel.addCardViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import com.johnri.labRatStudyCards.data.repository.studyCardRepository
import kotlinx.coroutines.launch


class addCardViewModel(
    private val repository: studyCardRepository
) : ViewModel() {

    fun addCard(
        questionHtml: String,
        answerHtml: String,
        chapterId: Int
    ) {
        viewModelScope.launch {
            val card = studyCardEntity(
                question = questionHtml,
                answer = answerHtml,
                chapterId = chapterId,
                nextReview = System.currentTimeMillis()
            )
            repository.insertCard(card)
        }
    }
}