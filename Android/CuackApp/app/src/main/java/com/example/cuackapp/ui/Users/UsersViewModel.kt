package com.example.cuackapp.ui.Users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.userModel.User
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

    val users: StateFlow<List<User>> = _users


    fun getUsers() {
        viewModelScope.launch {
            try {
                var result = withContext(Dispatchers.IO) {
                    apiService.getUsers()
                }
                Log.i("API_SUCCESS", "usuarios obtenidos: $result")

                Log.i("API_SUCCESS", "usuarios obtenidos: ${result.size}")


                for (user in result) {
                    if (user.type.id == 4){
                        //SI ES STUDENT, BUSCAR SU CICLO Y CURSO EN LA API Y ASIGNARLOS
                        val addresult = withContext(Dispatchers.IO) {
                            apiService.getCicloById(user.id)
                        }

                        Log.i("API_SUCCESS", "Ciclo obtenido: $addresult")
                        Log.i("API_SUCCESS", "Ciclos obtenidos: ${addresult.size}")
                        if (addresult.size > 0) {
                            user.cicloId = addresult.get(0).idCiclo
                            user.ciclo = addresult.get(0).nombreCiclo
                            user.curso = addresult.get(0).curso
                        }


                        Log.i("API_SUCCESS", "Ciclo añadido: ${user.ciclo} al usuario ${user.username}")
                    }
                }

               result = result.filter { user ->
                    user.username.length < 20
               }



                _users.value = result

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }
    }
}
