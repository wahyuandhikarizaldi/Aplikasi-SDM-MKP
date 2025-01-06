package com.example.androidmkp.ui.proyek

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmkp.api.ApiConfig
import com.example.androidmkp.model.Proyek
import com.example.androidmkp.model.SpreadsheetRow
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.repository.UserRepository
import kotlinx.coroutines.launch

class ProyekViewModel(private val repository: UserRepository) : ViewModel() {
    private val _proyekList = MutableLiveData<List<Proyek>>()
    val proyekList: LiveData<List<Proyek>> = _proyekList

    val status = MutableLiveData<String>()
    private val _spreadsheetData = MutableLiveData<List<SpreadsheetRow>>()
    val spreadsheetData: LiveData<List<SpreadsheetRow>> = _spreadsheetData

    fun fetchProyekData() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getProyekData()
                if (response.isSuccessful) {
                    _proyekList.postValue(response.body() ?: emptyList())
                }
            } catch (_: Exception) {
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun fetchSpreadsheetData(nid: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDataById(nid)
                if (response.isSuccessful) {
                    _spreadsheetData.postValue(response.body())
                }
            } catch (e: Exception) {
                Log.e("FetchSpreadsheetError", e.message ?: "Error occurred")
            }
        }
    }


    fun getStatus(): LiveData<String> {
        return status
    }

}





