package com.example.cuackapp.ui.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.UserModel.User

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    fun getLogin(username : String, password: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiService.getLogin(username, password)
                }
                Log.i("API_SUCCESS", "Usuario obtenido: $result")
                _user.value = result

            }catch (e: Exception){
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }

    }
}