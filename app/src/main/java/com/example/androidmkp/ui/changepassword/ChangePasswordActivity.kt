/**
 * Activity yang memungkinkan pengguna untuk mengubah kata sandi mereka.
 * Memvalidasi inputan, mengirim permintaan perubahan password ke server, dan menampilkan hasil.
 */

package com.example.androidmkp.ui.changepassword

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.R
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.ActivityChangePasswordBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.ui.login.LoginActivity
import com.example.androidmkp.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(application as Application)
        viewModel = ViewModelProvider(this, factory)[ChangePasswordViewModel::class.java]

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

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }

            binding.btnSubmit.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE

                val oldPassword = binding.edOldPassword.text.toString()
                val newPassword = binding.edNewPassword.text.toString()
                val confirmPassword = binding.edConfirmPassword.text.toString()

                if (checkInput(oldPassword, newPassword, confirmPassword, userSession.password)) {
                    binding.progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        try {
                            viewModel.postData(
                                userSession.nid, userSession.nama, userSession.birthdate,
                                userSession.phone, userSession.posisi, userSession.kota,
                                userSession.provinsi, newPassword, userSession.riwayatproyek
                            )

                            // Menampilkan dialog sukses setelah password berhasil diubah.
                            showAlertDialog(
                                title = "Berhasil!",
                                errorMessage = "Permintaan perubahan password anda akan diproses maksimal 2x24 jam.",
                                type = DialogType.SUCCESS,
                                icon = R.drawable.outline_how_to_reg_24,
                                doAction = {
                                    navigateToProfile()     // Navigasi ke halaman profil setelah sukses.
                                }
                            )
                        } catch (e: Exception) {

                            // Menampilkan dialog error jika terjadi kegagalan dalam proses.
                            showAlertDialog(
                                title = "Perubahan password gagal!",
                                errorMessage = "Silahkan coba lagi.",
                                type = DialogType.ERROR,
                                icon = R.drawable.baseline_error_outline_24,
                                doAction = {}
                            )
                        } finally {
                            clearForm() // Membersihkan form input setelah proses selesai.
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

            viewModel.getStatus().observe(this@ChangePasswordActivity) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInput(oldPassword: String, newPassword: String, confirmPassword: String, currentPassword: String): Boolean {

        var isValid = true

        // Memeriksa apakah password lama sesuai dengan yang ada di sistem.
        if (oldPassword != currentPassword) {
            showAlertDialog(
                title = "Password Salah!",
                errorMessage = "Password lama yang Anda masukkan salah.",
                type = DialogType.ERROR,
                icon = R.drawable.baseline_error_outline_24,
                doAction = {}
            )
            isValid = false
        }

        // Memeriksa panjang password lama.
        if (oldPassword.isEmpty() || oldPassword.length < 8) {
            binding.edOldPassword.error = getString(R.string.invalid_password_length)
            isValid = false
        }

        // Memeriksa panjang password baru.
        if (newPassword.isEmpty() || newPassword.length < 8) {
            binding.edNewPassword.error = getString(R.string.invalid_password_length)
            isValid = false
        }

        // Memeriksa kecocokan password baru dengan konfirmasi password.
        if (confirmPassword != newPassword) {
            binding.edConfirmPassword.error = getString(R.string.password_do_not_match)
            isValid = false
        }

        return isValid
    }

    enum class DialogType {
        ERROR,
        SUCCESS
    }

    private fun showAlertDialog(
        title: String,
        errorMessage: String,
        icon: Int,
        type: DialogType,
        doAction: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(errorMessage)
            setIcon(icon)
            setPositiveButton("OK") { _, _ ->
                if (type == DialogType.SUCCESS) {
                    doAction()
                }
            }
        }

        builder.create().apply {
            setCancelable(false)
            show()
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("fragment", "profile")
        }
        startActivity(intent)
    }

    private fun clearForm() {
        binding.edOldPassword.text?.clear()
        binding.edNewPassword.text?.clear()
        binding.edConfirmPassword.text?.clear()
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
