// Menghandle instance dari semua viewmodel
// ViewModelFactory adalah kelas yang digunakan untuk membuat instance ViewModel di dalam arsitektur MVVM (Model-View-ViewModel). Fungsinya adalah untuk menyediakan cara yang terstruktur dan konsisten dalam membuat dan menyediakan ViewModel yang memerlukan dependensi.

package com.example.androidmkp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidmkp.di.Injection
import com.example.androidmkp.repository.UserRepository
import com.example.androidmkp.ui.changepassword.ChangePasswordViewModel
import com.example.androidmkp.ui.editprofile.EditProfileViewModel
import com.example.androidmkp.ui.login.LoginViewModel
import com.example.androidmkp.ui.profile.ProfileViewModel
import com.example.androidmkp.ui.map.MapViewModel
import com.example.androidmkp.ui.detailproyek.DetailProyekViewModel
import com.example.androidmkp.ui.proyek.ProyekViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel() as T
            }
            modelClass.isAssignableFrom(ProyekViewModel::class.java) -> {
                ProyekViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(DetailProyekViewModel::class.java) -> {
                DetailProyekViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userRepository = Injection.provideUserRepository(context)
                ViewModelFactory(userRepository).also {
                    INSTANCE = it
                }
            }
        }
    }
}
