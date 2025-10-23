package com.example.pigg

data class Jugador(
    val id: Int,
    val nombre: String,
    var puntosTotales: Int = 0,
    var puntosTurno: Int = 0
)
