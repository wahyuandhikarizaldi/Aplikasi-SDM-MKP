package com.example.androidmkp.model

data class DetailKota(
    val nid: String,
    val parentNid: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val jumlahPekerja: Int,
    val jumlahWelder: Int,
    val jumlahFitter: Int,
    val jumlahHelper: Int,
    val jumlahMachinist: Int,
)


