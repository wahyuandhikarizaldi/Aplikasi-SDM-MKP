package com.example.androidmkp.model

data class DetailProvinsi(
    val nid: String,
    val name: String,
    val latitude: String,
    val longitude: String,
    val jumlahPekerja: Int,
    val jumlahWelder: Int,
    val jumlahFitter: Int,
    val jumlahHelper: Int,
    val jumlahMachinist: Int,
    val kota: List<Kota>
)

data class Kota(
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

