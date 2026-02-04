package com.example.cuackapp.ui.Profile

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.userModel.ProfilePicToJson
import com.example.cuackapp.model.userModel.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {


     fun updateToServer(currentuser:User, updateprofile: ProfilePicToJson) {
         viewModelScope.launch {
            try {
                val response = apiService.putUser(currentuser.id, updateprofile)
                Log.i("ProfileFragment", "Perfil actualizado correctamente: $response")

            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error de red: ${e.message}")
            }
        }
    }


}