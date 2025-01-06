// Konfigurasi dan penyedia layanan API di proyek Android yang menggunakan Retrofit untuk mengakses API
package com.example.androidmkp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object ApiConfig {

    // URL dasar untuk mengakses API Google Spreadsheet Apps Script.
    // Ganti BASE_URL ini dengan URL API deployment yang sesuai.
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxgadbOVbEVWslzIkc83R2lF-cZsJYWaYMlv7eXIpWo9CPndumK65n_82_Xjo9QOGCl/"

    /**
     * Fungsi untuk menyediakan instance ApiService.
     * ApiService mendefinisikan endpoint API yang akan diakses.
     */
    fun getApiService(): ApiService {
        // Logging interceptor untuk mencatat request dan response API dalam log.
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Menampilkan detail body dari request dan response.
        }

        // Konfigurasi HTTP client dengan timeout dan interceptor.
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Tambahkan logging interceptor ke OkHttpClient.
            .connectTimeout(120, TimeUnit.SECONDS) // Timeout untuk koneksi.
            .readTimeout(120, TimeUnit.SECONDS) // Timeout untuk membaca data dari server.
            .build()

        // Konfigurasi Gson untuk parsing JSON agar lebih toleran terhadap format tidak baku.
        val gson = GsonBuilder()
            .setLenient() // Membolehkan JSON dengan format yang longgar.
            .create()

        // Membangun instance Retrofit dengan BASE_URL, client, dan converter JSON.
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // URL dasar untuk API.
            .client(client) // HTTP client yang sudah dikonfigurasi.
            .addConverterFactory(GsonConverterFactory.create(gson)) // Konverter JSON ke objek Kotlin.
            .build()

        // Mengembalikan instance ApiService untuk digunakan di aplikasi.
        return retrofit.create(ApiService::class.java)
    }
}
