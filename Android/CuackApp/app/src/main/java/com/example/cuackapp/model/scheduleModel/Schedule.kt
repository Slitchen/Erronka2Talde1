package com.example.cuackapp.model.scheduleModel

import com.google.gson.annotations.SerializedName

data class Schedule(
    // SERIALIZEDNAME = el tag que recibimos de la api en cada variable, la cual podemos cambiar para gestion local
    @SerializedName("extra") val extra : String, // en caso de ser alumno sera el nombre del profe, de lo contrario el aula
    @SerializedName("modulo")val module: String, // asignatura
    @SerializedName("dia")val day : String,
    @SerializedName("hora")val hour : Int
)
