package com.example.androidmkp.model

data class TotalNasional(
    val totalPekerja: Int,
    val totalWelder: Int,
    val totalFitter: Int,
    val totalHelper: Int,
    val totalMachinist: Int,
    val provinsi: List<Provinsi>
)

data class Provinsi(
    val nid: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val jumlahPekerja: Int,
    val jumlahWelder: Int,
    val jumlahFitter: Int,
    val jumlahHelper: Int,
    val jumlahMachinist: Int
)
