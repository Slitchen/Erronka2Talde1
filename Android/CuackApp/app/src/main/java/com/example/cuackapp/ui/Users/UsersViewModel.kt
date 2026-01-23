package com.example.cuackapp.ui.Users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.UserModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())

    val users :  StateFlow<List<User>> = _users

    fun getUsers(){
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiService.getUsers()
                }
                Log.i("API_SUCCESS", "usuarios obtenidos: $result")

                _users.value = result

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }
    }
}