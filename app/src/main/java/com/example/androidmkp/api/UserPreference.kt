// Kelas ini bertanggung jawab untuk mengelola preferensi pengguna menggunakan Jetpack DataStore.
// Data disimpan dalam bentuk key-value pairs, menggantikan SharedPreferences untuk penyimpanan yang lebih modern.

package com.example.androidmkp.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.androidmkp.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NID_KEY] = user.nid
            preferences[NAMA_KEY] = user.nama
            preferences[BIRTHDATE_KEY] = user.birthdate
            preferences[PHONE_KEY] = user.phone
            preferences[POSISI_KEY] = user.posisi
            preferences[KOTA_KEY] = user.kota
            preferences[PROVINSI_KEY] = user.provinsi
            preferences[PASSWORD_KEY] = user.password
            preferences[RIWAYATPROYEK_KEY] = user.riwayatproyek
            preferences[IS_LOGIN_KEY] = true    // Menandai pengguna telah login.
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NID_KEY] ?: "",
                preferences[NAMA_KEY] ?: "",
                preferences[BIRTHDATE_KEY] ?: "",
                preferences[PHONE_KEY] ?: "",
                preferences[POSISI_KEY] ?: "",
                preferences[KOTA_KEY] ?: "",
                preferences[PROVINSI_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[RIWAYATPROYEK_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            val nid = preferences[NID_KEY]
            nid
        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NID_KEY = stringPreferencesKey("nid")
        private val NAMA_KEY = stringPreferencesKey("nama")
        private val BIRTHDATE_KEY = stringPreferencesKey("birthdate")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val POSISI_KEY = stringPreferencesKey("posisi")
        private val KOTA_KEY = stringPreferencesKey("kota")
        private val PROVINSI_KEY = stringPreferencesKey("provinsi")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val RIWAYATPROYEK_KEY = stringPreferencesKey("riwayatproyek")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}