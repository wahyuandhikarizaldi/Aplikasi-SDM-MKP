package com.example.androidmkp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmkp.api.ApiConfig
import com.example.androidmkp.model.DetailKota
import com.example.androidmkp.model.DetailProvinsi
import com.example.androidmkp.model.TotalNasional
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _totalNasional = MutableLiveData<TotalNasional>()
    val totalNasional: LiveData<TotalNasional> get() = _totalNasional

    private val _detailProvinsi = MutableLiveData<DetailProvinsi>()
    val detailProvinsi: LiveData<DetailProvinsi> get() = _detailProvinsi

    private val _detailKota = MutableLiveData<DetailKota>()
    val detailKota: LiveData<DetailKota> get() = _detailKota

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        fetchTotalNasionalData()
    }

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun fetchTotalNasionalData() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getTotalNasional()
                if (response.isSuccessful) {
                    _totalNasional.postValue(response.body())
                }
            } catch (_: Exception) {
            }
        }
    }

    fun fetchDetailProvinsi(nid: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDetailProvinsi(nid = nid)
                if (response.isSuccessful) {
                    _detailProvinsi.postValue(response.body())
                }
            } catch (_: Exception) {
            }
        }
    }

    fun fetchDetailKota(nid: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getDetailKota(nid = nid)
                if (response.isSuccessful) {
                    _detailKota.postValue(response.body())
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
