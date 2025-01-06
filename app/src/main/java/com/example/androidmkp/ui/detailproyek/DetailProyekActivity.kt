/**
 * Activity yang menampilkan detail dari suatu proyek ketika di klik.
 * mendaftar ke proyek.
 */

package com.example.androidmkp.ui.detailproyek

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.R
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.ActivityDetailProyekBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.model.Proyek
import com.example.androidmkp.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION", "SameParameterValue")
class DetailProyekActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProyekBinding
    private lateinit var viewModel: DetailProyekViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProyekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(application as Application)
        viewModel = ViewModelProvider(this, factory)[DetailProyekViewModel::class.java]

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        lifecycleScope.launch {
            val userPreference = Injection.provideUserPreference(applicationContext)
            val userSession = userPreference.getSession().first()

            val proyekData = intent.getParcelableExtra<Proyek>("proyekData")

            proyekData?.let { it ->
                binding.proyekName.text = it.proyek
                binding.kode.text = it.kode
                binding.penyelenggara.text = it.penyelenggara
                binding.lokasi.text = it.lokasi
                binding.kota.text = it.kota
                binding.slot.text = "${it.jumlahpekerja}/${it.totalslot}"
                binding.deskripsi.text = it.deskripsi
                binding.durasi.text = it.durasi
                val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val startDate = it.start?.let { it1 -> inputFormat.parse(it1) }
                val endDate = it.end?.let { it1 -> inputFormat.parse(it1) }

                binding.tanggalmulai.text = startDate?.let { outputFormat.format(it) } ?: it.start
                binding.tanggalakhir.text = endDate?.let { outputFormat.format(it) } ?: it.end

                val proyekKode = it.kode
                val riwayatProyekList = userSession.riwayatproyek.split(";")

                if (it.sisaslot == "0") {
                    binding.submitButton.isEnabled = false
                    binding.submitButton.text = "Slot Penuh"
                    binding.submitButton.setTextColor(Color.parseColor("#000000"))
                } else if (riwayatProyekList.contains(proyekKode)) {
                    binding.submitButton.isEnabled = false
                    binding.submitButton.text = "Sudah Terdaftar"
                    binding.submitButton.setTextColor(Color.parseColor("#000000"))

                } else {
                    binding.submitButton.setOnClickListener {
                        AlertDialog.Builder(this@DetailProyekActivity).apply {
                            setTitle("Anda yakin untuk mendaftar di pekerjaan ini?")
                            setMessage("Jika anda setuju untuk memilih pekerjaan ini, maka berarti anda setuju dengan segala persyaratan blablablabla di pekerjaan ini.")

                            setPositiveButton("Ya") { _, _ ->
                                val riwayatbaru = userSession.riwayatproyek + ";" + proyekKode
                                Log.d("riwayatbaru", riwayatbaru)
                                viewModel.postData(
                                    userSession.nid, userSession.nama, userSession.birthdate,
                                    userSession.phone, userSession.posisi, userSession.kota,
                                    userSession.provinsi, userSession.password, riwayatbaru
                                )

                                showAlertDialog(
                                    title = "Berhasil Mendaftar!",
                                    errorMessage = "Permintaan anda akan diproses maksimal 2x24 jam.",
                                    icon = R.drawable.outline_how_to_reg_24,
                                    doAction = {
                                        val intent = Intent(this@DetailProyekActivity, MainActivity::class.java)
                                        intent.putExtra("navigateTo", "ProyekFragment")
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                )
                            }

                            setNegativeButton("Batalkan") { dialog, _ ->
                                dialog.dismiss()
                            }

                            create()
                            show()
                        }
                    }
                }
            }

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun showAlertDialog(
        title: String,
        errorMessage: String,
        icon: Int,
        doAction: () -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(errorMessage)
            setIcon(icon)
            setPositiveButton("OK") { _, _ ->
                doAction()
            }
            create()
            show()
        }
    }
}
