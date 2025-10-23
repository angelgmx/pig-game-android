package com.example.pigg

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pigg.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Variables del juego
    private lateinit var jugadores: MutableList<Jugador>
    private var jugadorActualIndex = 0
    private var rondaActual = 1
    private var rondasTotales = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar spinners
        configurarSpinners()

        // Configurar botones
        binding.startButton.setOnClickListener { iniciarPartida() }
        binding.rollButton.setOnClickListener { lanzarDado() }
        binding.holdButton.setOnClickListener { mantenerPuntos() }

        // Inicializar UI
        actualizarUIInicial()
    }

    private fun configurarSpinners() {
        // Configurar spinner de jugadores
        val playersOptions = arrayOf("2 Jugadores", "3 Jugadores", "4 Jugadores")
        val playersAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, playersOptions)
        playersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.playersSpinner.adapter = playersAdapter

        // Configurar spinner de rondas
        val roundsOptions = arrayOf("Corta (2 rondas)", "Media (4 rondas)", "Larga (6 rondas)")
        val roundsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roundsOptions)
        roundsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roundsSpinner.adapter = roundsAdapter
    }

    private fun iniciarPartida() {
        // Obtener configuración desde los spinners
        // Selecionar numero de jugdores
        val numJugadores = when(binding.playersSpinner.selectedItemPosition) {
            0 -> 2
            1 -> 3
            else -> 4
        }

        rondasTotales = when(binding.roundsSpinner.selectedItemPosition) {
            0 -> 2 // Corta
            1 -> 4 // Media
            else -> 6 // Larga
        }

        // Crear jugadores
        jugadores = mutableListOf()
        for (i in 1..numJugadores) {
            jugadores.add(Jugador(i, "Jugador $i"))
        }

        // Resetear variables
        jugadorActualIndex = 0
        rondaActual = 1

        // Actualizar UI
        actualizarUI()
        binding.gameStatusText.text = "¡Partida iniciada! ${jugadores[jugadorActualIndex].nombre} comienza."

        // Habilitar botones del juego
        binding.rollButton.isEnabled = true
        binding.holdButton.isEnabled = true
    }

    private fun lanzarDado() {
        // Verificar que el juego esté iniciado
        if (!::jugadores.isInitialized) {
            binding.gameStatusText.text = "¡Primero presiona COMENZAR para iniciar el juego!"
            return
        }

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

        binding.diceImage.setImageResource(imagenId)

        // Obtener jugador actual
        val jugador = jugadores[jugadorActualIndex]

        if (numero == 1) {
            cambiarTurno()
            jugador.puntosTurno = 0
            cambiarTurno()
            binding.gameStatusText.text = "¡${jugador.nombre} sacó 1! Pierde los puntos del turno."
            actualizarUI()
            cambiarTurno()
        } else {
            jugador.puntosTurno += numero
            binding.gameStatusText.text = "¡${jugador.nombre} sacó $numero! Ahora tiene ${jugador.puntosTurno} puntos en este turno."
        }

        actualizarUI()
    }

    private fun mantenerPuntos() {
        // Verificar que el juego esté iniciado
        if (!::jugadores.isInitialized) {
            binding.gameStatusText.text = "¡Primero presiona COMENZAR para iniciar el juego!"
            return
        }

        val jugador = jugadores[jugadorActualIndex]

        if (jugador.puntosTurno > 0) {
            jugador.puntosTotales += jugador.puntosTurno
            jugador.puntosTurno = 0
            cambiarTurno()
            binding.gameStatusText.text = "¡Puntos guardados! Turno de ${jugadores[jugadorActualIndex].nombre}"
            actualizarUI()
        } else {
            binding.gameStatusText.text = "Tira el dado primero."
        }

        actualizarUI()
    }

    private fun cambiarTurno() {
        jugadorActualIndex = (jugadorActualIndex + 1) % jugadores.size

        // Si vuelve al primer jugador, avanza una ronda
        if (jugadorActualIndex == 0) {
            rondaActual++
        }
        actualizarUI()
    }

    private fun finalizarPartida() {
        val ganador = jugadores.maxByOrNull { it.puntosTotales }
        binding.gameStatusText.text = "${ganador?.nombre} gana con ${ganador?.puntosTotales} puntos!"
        binding.rollButton.isEnabled = false
        binding.holdButton.isEnabled = false
        binding.startButton.isEnabled = true

    }

    private fun actualizarUI() {
        if (::jugadores.isInitialized) {
            val jugador = jugadores[jugadorActualIndex]
            binding.roundInfo.text = "Ronda: $rondaActual / $rondasTotales"
            binding.playerInfo.text = "Jugador: ${jugador.nombre}"
            binding.turnScoreText.text = "Puntos del turno: ${jugador.puntosTurno}"

            // Si se superan las rondas totales, termina la partida
            if (rondaActual > rondasTotales) {
                finalizarPartida()
            }
        }
    }

    private fun actualizarUIInicial() {
        binding.roundInfo.text = "Ronda: -"
        binding.playerInfo.text = "Jugador: -"
        binding.turnScoreText.text = "Puntos del turno: -"
        binding.gameStatusText.text = "¡Configura el juego y presiona COMENZAR para iniciar!"
        binding.rollButton.isEnabled = false
        binding.holdButton.isEnabled = false
        binding.startButton.isEnabled = true
    }
}