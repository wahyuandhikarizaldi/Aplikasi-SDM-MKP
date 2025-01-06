package com.example.androidmkp.ui.detailproyek

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmkp.api.ApiConfig
import com.example.androidmkp.model.SpreadsheetRow
import kotlinx.coroutines.launch

class DetailProyekViewModel : ViewModel() {
    val status = MutableLiveData<String>()
    private val _spreadsheetData = MutableLiveData<List<SpreadsheetRow>>()

    fun postData(
        nid: String,
        nama: String,
        birthdate: String,
        posisi: String,
        unit: String,
        kota: String,
        provinsi: String,
        password: String,
        riwayatproyek: String
    ) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().postData(nid, nama, birthdate, posisi, unit, kota, provinsi, password, riwayatproyek)
                if (response.isSuccessful) {
                    status.postValue(response.body()?.status)
                    fetchSpreadsheetData(nid)
                }
            } catch (e: Exception) {
                Log.e("PostDataError", e.message ?: "Error occurred")
            }
        }
    }

    private fun fetchSpreadsheetData(nid: String) {
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
}