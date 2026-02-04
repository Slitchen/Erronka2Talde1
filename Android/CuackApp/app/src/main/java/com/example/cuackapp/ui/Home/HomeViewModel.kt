package com.example.cuackapp.ui.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class HomeViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules

    // Prioridad de días para el ordenamiento
    private val diasPrioridad = mapOf(
        "LUNES" to 1, "MARTES" to 2, "MIERCOLES" to 3,"JUEVES" to 4, "VIERNES" to 5
    )


    //Obtener horarios según el tipo de usuario
    fun getSchedules(currentUser: User) {
        viewModelScope.launch {
            try {
                val result = when(currentUser.type.id) {
                    //profesor
                    3 -> withContext(Dispatchers.IO) {
                        apiService.getHorariosProfesor(currentUser.id)
                    }
                    //alumno
                    4 ->  withContext(Dispatchers.IO) {
                            apiService.getHorariosAlumno(currentUser.id)
                    }

                    else ->emptyList()
                } as List<Schedule>


                Log.i("API_SUCCESS", "Horarios obtenidos: $result")

                // Ordenar: 1º por Día, 2º por Hora
                val sortedList = result.sortedWith(
                    compareBy<Schedule> { diasPrioridad[it.day.toString().uppercase()] ?: 99 }
                        .thenBy { it.hour }
                )
                Log.i("API_SUCCESS", "Horarios ordenados: $sortedList")
                _schedules.value = sortedList

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }
    }
}