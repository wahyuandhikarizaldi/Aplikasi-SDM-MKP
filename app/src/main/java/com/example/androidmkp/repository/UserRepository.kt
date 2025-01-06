/**
 * UserRepository bertindak sebagai lapisan abstraksi antara sumber data (API atau lokal)
 * dan ViewModel atau Presenter. Repository ini bertanggung jawab untuk mengelola
 * logika data terkait pengguna, seperti sesi login, logout, dan pengambilan data pengguna.
 */

package com.example.androidmkp.repository

import com.example.androidmkp.api.ApiService
import com.example.androidmkp.model.LoginResponse
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.api.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    // Menyimpan data sesi pengguna ke UserPreference.
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    // Melakukan proses login dengan mengirimkan NID dan password ke server.
    suspend fun login(nid: String, password: String): LoginResponse {
        return try {
            val action = "login"
            val response = apiService.login(action, nid, password)
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw e
        }
    }

    // Mengambil data sesi pengguna yang tersimpan dalam UserPreference.
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    // Menghapus data sesi pengguna dari UserPreference.
    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
