// Object Injection bertindak sebagai penyedia dependensi (Dependency Injection).
// Mempermudah pengelolaan dan inisialisasi objek seperti ApiService, UserPreference, dan UserRepository.

package com.example.androidmkp.di

import android.content.Context
import com.example.androidmkp.api.ApiService
import com.example.androidmkp.api.ApiConfig
import com.example.androidmkp.api.UserPreference
import com.example.androidmkp.api.dataStore
import com.example.androidmkp.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    private fun provideApiService(context: Context): ApiService {
        val pref = provideUserPreference(context)
        runBlocking { pref.getToken().first() ?: "" }
        return ApiConfig.getApiService()
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = provideUserPreference(context)
        val apiService = provideApiService(context)
        return UserRepository.getInstance(pref, apiService)
    }

}