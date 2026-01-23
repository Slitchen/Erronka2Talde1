package com.example.cuackapp.data.api

import com.example.cuackapp.model.UserModel.User
import com.example.cuackapp.model.scheduleModel.Schedule
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("horariosOrdenados/{id}")
    suspend fun getHorarios(
        @Path("id") id: Int
    ): List<Schedule>//poner user y quitar list

    @GET("usersAll")
    suspend fun getUsers(): List<User>

    @GET("login/{username}/{password}")
    suspend fun getLogin(
        @Path("username") username: String,
        @Path("password") password: String
    ): User
}