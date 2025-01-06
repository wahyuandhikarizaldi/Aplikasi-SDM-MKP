/**
 * MainActivity menghandle navigasi dari activity-activity lainnya.
 */

package com.example.androidmkp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.R
import com.example.androidmkp.databinding.ActivityMainBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.ui.login.LoginActivity
import com.example.androidmkp.ui.map.MapFragment
import com.example.androidmkp.ui.myjobs.MyJobsFragment
import com.example.androidmkp.ui.profile.ProfileFragment
import com.example.androidmkp.ui.proyek.ProyekFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProyekFragment()).commit()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "ProyekFragment") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProyekFragment())
                .commit()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        lifecycleScope.launch {
            val userPreference = Injection.provideUserPreference(applicationContext)
            val userSession = userPreference.getSession().first()

            if (!userSession.isLogin) {
                navigateToWelcome()
                return@launch
            }
        }
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProyekFragment()).commit()
                return true
            }
            R.id.nav_discover -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MapFragment()).commit()
                return true
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
                return true
            }
            R.id.nav_myjobs -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MyJobsFragment()).commit()
                return true
            }
        }
        return false
    }
}
