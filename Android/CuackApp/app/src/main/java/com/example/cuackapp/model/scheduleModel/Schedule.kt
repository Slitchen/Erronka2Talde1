package com.example.cuackapp.model.scheduleModel

import android.R
import com.example.cuackapp.model.UserModel.User
import com.example.cuackapp.model.moduleModel.Module
import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("nombre") val teacher : String,
    @SerializedName("modulo")val module: String,
    @SerializedName("dia")val day : String,
    @SerializedName("hora")val hour : Int
)
