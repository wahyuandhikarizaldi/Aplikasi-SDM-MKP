package com.example.androidmkp.model

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)

data class LoginResult(
    val nid: String,
    val nama: String,
    val birthdate: String,
    val phone: String,
    val posisi: String,
    val kota: String,
    val provinsi: String,
    val password: String,
    val riwayatproyek: String
)
