package com.example.cuackapp.model.UserModel

import com.google.gson.annotations.SerializedName


data class User(
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val nombre: String,
    val apellidos: String,
    val dni: String,
    val direccion: String,
    val telefono1: String,
    val telefono2: String,

    @SerializedName("tipos")
    val type: UserType,

    val createdAt: String,
    val updatedAt: String
)

data class UserType(
    val id: Int,
    val name: String,
    val nameEu: String
)