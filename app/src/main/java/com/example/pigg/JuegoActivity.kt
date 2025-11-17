package com.example.pigg

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pigg.databinding.ActivityJuegoBinding
import kotlin.random.Random

class JuegoActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityJuegoBinding

    // Variables del juego
    private lateinit var jugadores: MutableList<Jugador>
    private var jugadorActualIndex = 0
    private var rondaActual = 1
    private var rondasTotales = 2
    private var numRondas = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inicializamos el binding
        binding = ActivityJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Configurar botones
        binding.botonLanzar.setOnClickListener { lanzarDado() }
        binding.botonMantener.setOnClickListener { mantenerPuntos() }

        // Recibir datos del Intent y iniciar partida
        recibirDatosEIniciar()
    }

    private fun recibirDatosEIniciar() {


        // inicializar la lista jugadores
        jugadores = mutableListOf()

        // Recibir datos del Intent
        val bundle = intent.extras
        val nombresJugadores = bundle?.getStringArrayList("NOMBRES_JUGADORES")

        // Leer el número de rondas
        rondasTotales = bundle?.getInt("NUM_RONDAS") ?: 2

        // Configurar rondas totales
        numRondas = rondasTotales


        // Crear jugadores con los nombres recibidos
        nombresJugadores?.forEachIndexed { i, nombre ->
            val nombre = nombresJugadores[i];
            val jugadorNuevo = Jugador(i, nombre)
            jugadores.add(jugadorNuevo)
        }

        //  Barajar el orden de los jugadores aleatoriamente
        jugadores.shuffle()

        // Resetear variables
        jugadorActualIndex = 0
        rondaActual = 1

        // Actualizar pantalla e iniciar juego
        actualizarPantalla()
        binding.textoEstadoJuego.text = "¡Partida iniciada! ${jugadores[jugadorActualIndex].nombre} comienza."
    }


    private fun lanzarDado() {

        // Número aleatorio entre 1 y 6
        val numero = Random.nextInt(1, 7)

        // Cambiar imagen del dado
        val imagenId = when(numero) {
            1 -> R.drawable.dado1
            2 -> R.drawable.dado2
            3 -> R.drawable.dado3
            4 -> R.drawable.dado4
            5 -> R.drawable.dado5
            else -> R.drawable.dado6
        }

        binding.imagenDado.setImageResource(imagenId)

        // Obtener jugador actual
        val jugador = jugadores[jugadorActualIndex]

        if (numero == 1) {
            jugador.puntosTurno = 0
            cambiarTurno()
            binding.textoEstadoJuego.text = "¡${jugador.nombre} sacó 1! PIERDE los PUNTOS DE ESTE TURNO JAJAJAJ."
            actualizarPantalla()
        } else {
            jugador.puntosTurno += numero
            binding.textoEstadoJuego.text = "¡${jugador.nombre} sacó $numero! Ahora tiene ${jugador.puntosTurno} puntos en este turno."
        }
        actualizarPantalla()
    }

    private fun mantenerPuntos() {

        if (jugadores[jugadorActualIndex].puntosTurno > 0) {
            // Guardamos los puntos totales del jugador
            jugadores[jugadorActualIndex].puntosTotales += jugadores[jugadorActualIndex].puntosTurno
            // reiniciar a 0 los puntos para la siguiente ronda
            jugadores[jugadorActualIndex].puntosTurno = 0
            cambiarTurno()
            binding.textoEstadoJuego.text = "¡Puntos guardados!"
        } else {
            binding.textoEstadoJuego.text = "Tira el dado primero."
        }
        actualizarPantalla()
    }

    private fun cambiarTurno() {
        // cambiamos el jugador actual
        jugadorActualIndex = (jugadorActualIndex + 1) % jugadores.size

        // Si vuelve al primer jugador, avanza una ronda
        if (jugadorActualIndex == 0) {

            if (rondaActual >= rondasTotales){
                finalizarPartida()
                return
            } else {
                rondaActual++
            }
        }
        actualizarPantalla()
        binding.textoEstadoJuego.text = "Comieza el turno de ${jugadores[jugadorActualIndex].nombre}"
    }


    private fun actualizarPantalla() {
        if (::jugadores.isInitialized) {
            val jugador = jugadores[jugadorActualIndex]
            binding.infoRonda.text = "Ronda: $rondaActual / $rondasTotales"
            binding.infoJugador.text = "Jugador: ${jugador.nombre}"
            binding.textoPuntosTurno.text = "Puntos del turno: ${jugador.puntosTurno}"

            // Actualizar tablero de puntuaciones
            actualizarTableroPuntuaciones()

            // Si se superan las rondas totales, termina la partida
            if (rondaActual > rondasTotales) {
                finalizarPartida()
            }
        }
    }

    private fun finalizarPartida() {
        // Prevenir múltiples llamadas
        if (!::jugadores.isInitialized || jugadores.isEmpty()) return

        val ganador = jugadores.maxByOrNull { it.puntosTotales }
        val nombreGanador = ganador?.nombre

        // Generar el tablero de puntuaciones final
        val tableroFinal = StringBuilder()

        for (i in 0 until jugadores.size) {
            val jugador = jugadores[i]
            tableroFinal.append("${jugador.nombre}: ${jugador.puntosTotales} puntos\n")
        }

        val intent = Intent(this, ResultadosActivity::class.java)
        intent.putExtra("GANADOR", nombreGanador)
        intent.putExtra("TABLERO", tableroFinal.toString())
        startActivity(intent)
        finish()
    }

    private fun actualizarTableroPuntuaciones() {
       var tablero = ""
        for (i in 0 until jugadores.size) {
            // nombre del jugador
            val nombre = jugadores[i].nombre
            // puntos del jugador totales
            val puntosTotales = jugadores[i].puntosTotales
            // meter al tablero las puntuaciones
            if (i == jugadorActualIndex) {
                tablero += "- $nombre: $puntosTotales puntos\n"
            } else {
                tablero += " $nombre: $puntosTotales puntos\n"
            }
            binding.tableroPuntuaciones.text = tablero
        }
    }
}