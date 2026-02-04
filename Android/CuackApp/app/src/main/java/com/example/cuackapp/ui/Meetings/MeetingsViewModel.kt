package com.example.cuackapp.ui.Meetings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuackapp.data.api.ApiService
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.userModel.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MeetingsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings


    fun getMeetings(currentUser: User) {
        viewModelScope.launch {
            try {
                val fechas = listOf("2026-01-26", "2026-01-27", "2026-01-28", "2026-01-29", "2026-01-30")
                val result = when(currentUser.type.id) {
                    //profesor
                    3 -> withContext(Dispatchers.IO) {
                        fechas.flatMap { fecha ->
                            try {
                                apiService.getReunionesProfe(currentUser.id, fecha)
                            } catch (e: Exception) {
                                Log.e("API_ERROR", "Error en fecha $fecha: ${e.message}")
                                emptyList() // Si una fecha falla (ej. el BEGIN_OBJECT), devolvemos lista vacía y seguimos
                            }
                        }
                    }
                    //alumno
                    4 ->  withContext(Dispatchers.IO) {
                        fechas.flatMap { fecha ->
                            try {
                                apiService.getReunionesAlumno(currentUser.id, fecha)
                            } catch (e: Exception) {
                                Log.e("API_ERROR", "Error en fecha $fecha: ${e.message}")
                                emptyList() // Si una fecha falla (ej. el BEGIN_OBJECT), devolvemos lista vacía y seguimos
                            }
                        }
                    }

                    else ->emptyList()
                } as List<Meeting>

                Log.i("API_SUCCESS", "Reuniones obtenidas: $result")
                Log.i("API_SUCCESS", "date : ${result.get(0).scheduled_date}")
                Log.i("API_SUCCESS", "state : ${result.count()}")




                _meetings.value = result

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
            }
        }
    }
    //convierte una reunion en un json y lo manda a la api para crearla en la bbdd
    fun createMeeting(meeting: Meeting) {
        viewModelScope.launch {
            try {
                Log.i("API_SUCCESS", "json meeting: ${Gson().toJson(meeting)}")
                val response = withContext(Dispatchers.IO) {
                    apiService.postMeeting(meeting)
                }


                Log.i("API_SUCCESS", "Reunión creada: $response")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al crear reunión: ${e.message}")
            }
        }

    }
    fun updateMeeting(meeting: Meeting, currentUser: User) {
        viewModelScope.launch {
            try {
                val body = mapOf("estado" to meeting.state)
                Log.i("API_SUCCESS", "json meeting: ${Gson().toJson(body)}")
                Log.i("API_SUCCESS", "json meeting: ${meeting.id}")
                val response = withContext(Dispatchers.IO) {
                    apiService.putMeeting(meeting.id, body)
                }
                if (response.isSuccessful) {
                    getMeetings(currentUser)
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al actualizar reunión: ${e.message}")
            }
        }
    }
}

