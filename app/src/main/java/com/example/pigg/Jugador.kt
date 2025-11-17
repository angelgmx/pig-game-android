package com.example.pigg
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jugador(
    val id: Int,
    val nombre: String,
    var puntosTotales: Int = 0,
    var puntosTurno: Int = 0
) : Parcelable