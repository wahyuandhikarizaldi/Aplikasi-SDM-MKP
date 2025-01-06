package com.example.androidmkp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(nid: String, password: String) = liveData {
        try {
            val response = repository.login(nid, password)
            if (response.error) {
                throw Exception(response.message)
            } else {
                emit(response.loginResult)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}