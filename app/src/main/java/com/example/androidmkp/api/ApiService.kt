//  antarmuka untuk mendefinisikan metode-metode HTTP yang digunakan oleh aplikasi Android untuk berkomunikasi dengan API menggunakan Retrofit

package com.example.androidmkp.api

import com.example.androidmkp.model.DetailKota
import com.example.androidmkp.model.DetailProvinsi
import com.example.androidmkp.model.LoginResponse
import com.example.androidmkp.model.PostResponse
import com.example.androidmkp.model.Proyek
import com.example.androidmkp.model.SpreadsheetRow
import com.example.androidmkp.model.TotalNasional
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("exec")
    suspend fun postData(
        @Field("nid") nid: String,
        @Field("nama") nama: String,
        @Field("birthdate") birthdate: String,
        @Field("phone") phone: String,
        @Field("posisi") posisi: String,
        @Field("kota") kota: String,
        @Field("provinsi") provinsi: String,
        @Field("password") password: String,
        @Field("riwayatproyek") riwayatproyek: String
    ): Response<PostResponse>

    @FormUrlEncoded
    @POST("exec")
    suspend fun login(
        @Field("action") action: String,
        @Field("nid") nid: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("exec")
    suspend fun getDataById(
        @Query("nid") nid: String
    ): Response<List<SpreadsheetRow>>

    @GET("exec")
    suspend fun getProyekData(
        @Query("action") nid: String = "getProyek"
    ): Response<List<Proyek>>

    @GET("exec")
    suspend fun getTotalNasional(
        @Query("action") action: String = "getTotalNasional"
    ): Response<TotalNasional>

    @GET("exec")
    suspend fun getDetailProvinsi(
        @Query("action") action: String = "getDetailProvinsi",
        @Query("nid") nid: String
    ): Response<DetailProvinsi>

    @GET("exec")
    suspend fun getDetailKota(
        @Query("action") action: String = "getDetailKota",
        @Query("nid") nid: String
    ): Response<DetailKota>

}

