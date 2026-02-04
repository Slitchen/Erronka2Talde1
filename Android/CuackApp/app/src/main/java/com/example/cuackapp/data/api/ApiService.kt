package com.example.cuackapp.data.api

import retrofit2.Response
import com.example.cuackapp.model.meetingModel.Meeting
import com.example.cuackapp.model.userModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import com.example.cuackapp.model.userModel.ProfilePicToJson
import com.example.cuackapp.model.userModel.StudentInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    //SERVICIO QUE INSTANCIA LAS LLAMADAS A LA API

    //GET = select
    //POST = insert
    //PUT = update

    // Path = lo que le mandamos por ruta
    // Body = lo que le mandamos, no por ruta, cuerpos complejos como json
    // Multipart y Part = intento de subir foto, funciona si es en el dispositivo local
    @GET("horarioAlumno/{id}")
    suspend fun getHorariosAlumno(
        @Path("id") id: Int
    ): List<Schedule>

    @GET("horarioProfesor/{id}")
    suspend fun getHorariosProfesor(
        @Path("id") id: Int
    ): List<Schedule>

    @GET("usersAll")
    suspend fun getUsers(): List<User>

    @GET("login/{username}/{password}")
    suspend fun getLogin(
        @Path("username") username: String,
        @Path("password") password: String
    ): User

    @GET("reunionAlumno/{id}/{fecha}")//año/mes/dia
    suspend fun getReunionesAlumno(
        @Path("id") id: Int,
        @Path("fecha") fecha: String
    ): List<Meeting>

    @GET("reunionProfesor/{id}/{fecha}")//año/mes/dia
    suspend fun getReunionesProfe(
        @Path("id") id: Int,
        @Path("fecha") fecha: String
    ): List<Meeting>

    @GET("ikasleCiclo/{id}")
    suspend fun getCicloById(
        @Path("id") id: Int,
    ): List<StudentInfo>

    // Quita el /{meeting} de la ruta
    @POST("addReunion")
    suspend fun postMeeting(
        @Body meeting: Meeting // Retrofit convertirá el objeto a JSON automáticamente
    )

    @PUT("updateReunion/{id}")
    suspend fun putMeeting(
        @Path("id") id: Int,
        @Body body: Map<String, String>
    ): Response<Unit>
    @Multipart
    @PUT("updateUserFoto/{id}")
    suspend fun putUser(
        @Path("id") id: Int,
        @Part fotoUpdate: ProfilePicToJson
    )


}