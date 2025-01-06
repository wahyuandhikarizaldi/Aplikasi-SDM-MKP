/**
 * Activity yang menghandle page login
 */

package com.example.androidmkp.ui.login

import com.example.androidmkp.ui.main.MainActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.R
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.databinding.ActivityLoginBinding
import com.example.androidmkp.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val userPreference = Injection.provideUserPreference(applicationContext)
            val userSession = userPreference.getSession().first()

            if (userSession.isLogin) {
                navigateToMain()
                return@launch
            }
            val viewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
            viewModel = ViewModelProvider(this@LoginActivity, viewModelFactory)[LoginViewModel::class.java]
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val nid = binding.edLoginNid.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (checkInput(nid)) {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.login(nid, password).observe(this) { user ->
                    Log.d("user", "${user?.nid}, ${user?.nama}, ${user?.birthdate}, ${user?.phone}, ${user?.riwayatproyek}")
                    binding.progressBar.visibility = View.GONE
                    if (user != null) {
                        viewModel.saveSession(UserModel(user.nid, user.nama, user.birthdate, user.phone, user.posisi, user.kota, user.provinsi, user.password, user.riwayatproyek, true))
                        Log.d("user", "${user.nid}, ${user.nama}, ${user.birthdate}, ${user.phone}, ${user.riwayatproyek}")
                        showSuccessDialog()
                    } else {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun checkInput(nid: String): Boolean {
        var isValid = true
        if (nid.isEmpty()) {
            binding.edLoginNid.error = getString(R.string.invalid_nid)
            isValid = false
        }
        return isValid
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Login berhasil!")
            setMessage("Selamat datang di aplikasi MKP!")
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Login gagal!")
            setMessage("Harap pastikan nid dan password anda benar!")
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}