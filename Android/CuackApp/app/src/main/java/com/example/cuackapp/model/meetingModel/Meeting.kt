package com.example.cuackapp.model.meetingModel

import com.google.gson.annotations.SerializedName

class Meeting(
    // SERIALIZEDNAME = el tag que recibimos de la api en cada variable, la cual podemos cambiar para gestion local
    @SerializedName("idReunion") var id : Int,
    @SerializedName("estado")var state : String,
    @SerializedName("idProfesor") var profesor_id : Int,
    @SerializedName("idAlumno") var alum :Int,
    @SerializedName("fecha")var scheduled_date : String,
    @SerializedName("titulo")var title : String,
    @SerializedName("asunto")var description : String,
    @SerializedName("aula")var aula : String,
    @SerializedName("idCentro")var centro : String

){}