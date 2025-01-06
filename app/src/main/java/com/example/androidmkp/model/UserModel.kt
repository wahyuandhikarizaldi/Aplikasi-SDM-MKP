package com.example.androidmkp.model

data class UserModel(
    val nid: String,
    val nama: String,
    val birthdate: String,
    val phone: String,
    val posisi: String,
    val kota: String,
    val provinsi: String,
    val password: String,
    val riwayatproyek: String,
    val isLogin: Boolean = false
)