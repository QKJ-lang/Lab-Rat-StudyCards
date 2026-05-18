package com.johnri.labRatStudyCards.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnri.labRatStudyCards.data.entity.userEntity
import com.johnri.labRatStudyCards.data.repository.userRepository
import kotlinx.coroutines.launch

class authViewModel(
    private val repository: userRepository
) : ViewModel() {

    private val _user = MutableLiveData<userEntity?>()
    val user: LiveData<userEntity?> = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _user.value = repository.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _user.postValue(null)
            return
        }

        viewModelScope.launch {

            val result = repository.registerUser(
                userEntity(name = name, email = email, password = password)
            )

            if (result > 0) {
                _user.postValue(
                    userEntity(result.toInt(), name, email, password)
                )
            } else {
                _user.postValue(null)
            }
        }
    }
}