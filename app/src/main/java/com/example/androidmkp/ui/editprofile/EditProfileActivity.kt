/**
 * Activity yang memungkinkan pengguna untuk mengubah profile mereka.
 * mengubah nama, tanggal lahir, nomor telepon, posisi, kota, dan provinsi.
 */

package com.example.androidmkp.ui.editprofile

import android.annotation.SuppressLint
import com.example.androidmkp.ui.main.MainActivity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.R
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.ActivityEditProfileBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    private var birthdate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(application as Application)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]

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

            if (!userSession.isLogin) {
                navigateToWelcome()
                return@launch
            }

            populateProfileData(userSession)

            binding.etBirthdate.setOnClickListener {
                showDatePickerDialog()
            }

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }

            val posisiArray = resources.getStringArray(R.array.posisi_array)
            val tvPosisi = binding.tvPosisi

            binding.tvPosisi.setOnClickListener {
                val posisiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, posisiArray)
                binding.tvPosisi.setAdapter(posisiAdapter)
                binding.tvPosisi.showDropDown()
            }

            val kotaArray = resources.getStringArray(R.array.kota_array)
            val tvKota = binding.tvKota

            binding.tvKota.setOnClickListener {
                val editKotaAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, kotaArray)
                binding.tvKota.setAdapter(editKotaAdapter)
                binding.tvKota.showDropDown()
            }

            val provinsiArray = resources.getStringArray(R.array.provinsi_array)
            val tvProvinsi = binding.tvProvinsi

            binding.tvProvinsi.setOnClickListener {
                val editProvinsiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, provinsiArray)
                binding.tvProvinsi.setAdapter(editProvinsiAdapter)
                binding.tvProvinsi.showDropDown()
            }

            tvPosisi.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        val posisiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, kotaArray)
                        tvPosisi.setAdapter(posisiAdapter)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            tvKota.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        val editKotaAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, kotaArray)
                        tvKota.setAdapter(editKotaAdapter)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            tvProvinsi.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        val editProvinsiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, provinsiArray)
                        tvKota.setAdapter(editProvinsiAdapter)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.btnCheck.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE

                val nama = binding.etNama.text.toString()
                val displayedBirthdate = binding.etBirthdate.text.toString()
                val formattedBirthdate = displayedBirthdate.replace("/", "")
                val phone = binding.etPhone.text.toString()
                val posisi = binding.tvPosisi.text.toString()
                val kota = binding.tvKota.text.toString()
                val provinsi = binding.tvProvinsi.text.toString()

                viewModel.postData(userSession.nid, nama, formattedBirthdate, phone, posisi, kota, provinsi, userSession.password, userSession.riwayatproyek)
                clearForm()
                binding.progressBar.visibility = View.GONE
                navigateToProfile()
            }

            viewModel.getStatus().observe(this@EditProfileActivity) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateProfileData(userSession: UserModel) {
        binding.etNama.setText(userSession.nama)

        val birthdate = userSession.birthdate
        val formattedBirthdate = "${birthdate.substring(0, 2)}/${birthdate.substring(2, 4)}/${birthdate.substring(4)}"
        binding.etBirthdate.setText(formattedBirthdate)

        binding.tvPosisi.setText(userSession.posisi)
        binding.tvPosisi.setOnClickListener {
            val posisiArray = resources.getStringArray(R.array.posisi_array)
            val posisiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, posisiArray)
            binding.tvPosisi.setAdapter(posisiAdapter)
            binding.tvPosisi.showDropDown()
        }

        binding.tvKota.setText(userSession.kota)
        binding.tvKota.setOnClickListener {
            val kotaArray = resources.getStringArray(R.array.kota_array)
            val editKotaAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, kotaArray)
            binding.tvKota.setAdapter(editKotaAdapter)
            binding.tvKota.showDropDown()
        }

        binding.tvProvinsi.setText(userSession.provinsi)
        binding.tvProvinsi.setOnClickListener {
            val provinsiArray = resources.getStringArray(R.array.provinsi_array)
            val editProvinsiAdapter = ArrayAdapter(this@EditProfileActivity, R.layout.dropdown_item, provinsiArray)
            binding.tvProvinsi.setAdapter(editProvinsiAdapter)
            binding.tvProvinsi.showDropDown()
        }

        binding.etPhone.setText(userSession.phone)
    }

    private fun navigateToProfile() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("fragment", "profile")
        }
        startActivity(intent)
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                birthdate = String.format("%02d%02d%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etBirthdate.setText(String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear))
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun clearForm() {
        binding.etNama.text.clear()
        binding.etBirthdate.text.clear()
        binding.etPhone.text.clear()
        binding.tvPosisi.text.clear()
        binding.tvKota.text.clear()
        binding.tvProvinsi.text.clear()
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
