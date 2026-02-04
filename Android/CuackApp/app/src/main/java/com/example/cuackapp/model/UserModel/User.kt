package com.example.cuackapp.model.userModel

import com.google.gson.annotations.SerializedName


data class User(
    // SERIALIZEDNAME = el tag que recibimos de la api en cada variable, la cual podemos cambiar para gestion local

    var id: Int,
    var email: String,
    var username: String,
    var password: String,
    var nombre: String,
    var apellidos: String,
    var dni: String,
    var direccion: String,
    var telefono1: String,
    var telefono2: String,
    @SerializedName("argazkiaUrl")var profilePicture: String,
    var cicloId: Int,
    var ciclo: String,
    var curso: String,


    @SerializedName("tipos")
    var type: UserType,

    var createdAt: String,
    var updatedAt: String
)

data class UserType(
    var id: Int,
    var name: String,
    var nameEu: String
)