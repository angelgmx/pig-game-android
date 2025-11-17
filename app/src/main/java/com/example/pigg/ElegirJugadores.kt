package com.example.pigg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pigg.databinding.ActivityRecyclerviewJugadoresBinding

class ElegirJugadores : AppCompatActivity() {

    private var numJugadores = 2
    private var numRondas = 2
    private lateinit var binding: ActivityRecyclerviewJugadoresBinding

    private val todosLosNombres = listOf(
        "Aitor Tilla", "Ana Conda", "Armando Broncas", "Aurora Boreal", "Bartolo Mesa",
        "Carmen Mente", "Elba Lazo", "Enrique Cido", "Esteban Dido", "Fermin Tado",
        "Lola Mento", "Luz Cuesta", "Paco Tilla", "Pere Gil", "Salvador Tumbado"
    )

    private val nombresElegidos = mutableListOf<String?>()
    private val adaptadores = mutableListOf<NombreAdapter>()
    private lateinit var titulos: List<TextView>
    private lateinit var recyclers: List<RecyclerView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewJugadoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titulos = listOf(binding.tituloJugador1, binding.tituloJugador2, binding.tituloJugador3, binding.tituloJugador4)
        recyclers = listOf(binding.r1, binding.r2, binding.r3, binding.r4)

        recibirDatos()
        configurarVistasParaJugadores()
    }

    private fun recibirDatos() {
        val bundle = intent.extras
        if (bundle != null) {
            numJugadores = bundle.getInt("NUM_JUGADORES", 2)
            numRondas = bundle.getInt("NUM_RONDAS", 2)
        }
        nombresElegidos.addAll(List(numJugadores) { null })
    }

    private fun configurarVistasParaJugadores() {
        val nombresDisponibles = todosLosNombres.toMutableList()

        for (i in 0 until numJugadores) {
            titulos[i].visibility = View.VISIBLE
            recyclers[i].visibility = View.VISIBLE

            val adapter = NombreAdapter(nombresDisponibles.toList()) { nombreSeleccionado ->
                onNombreSeleccionado(i, nombreSeleccionado)
            }
            adaptadores.add(adapter)

            recyclers[i].layoutManager = LinearLayoutManager(this)
            recyclers[i].adapter = adapter
        }
    }

    private fun onNombreSeleccionado(jugadorIndex: Int, nombre: String) {
        if (nombresElegidos.contains(nombre)) {
            Toast.makeText(this, "Este nombre ya esté elegido, elige otro", Toast.LENGTH_SHORT).show()
            return
        }
        // 1. Guardar el nombre elegido.
        nombresElegidos[jugadorIndex] = nombre

        // 2. Actualizar la UI para este jugador.
        titulos[jugadorIndex].text = "Jugador ${jugadorIndex + 1}: $nombre"
        recyclers[jugadorIndex].visibility = View.GONE

        // 4. Comprobar si todos han elegido.
        comprobarSiTodosHanElegido()
    }


    private fun comprobarSiTodosHanElegido() {
        // Si no queda ningún `null` en la lista, significa que todos han elegido.
        if (!nombresElegidos.contains(null)) {
            val intent = Intent(this, JuegoActivity::class.java).apply {
                putStringArrayListExtra("NOMBRES_JUGADORES", ArrayList(nombresElegidos.filterNotNull()))
                putExtra("NUM_RONDAS", numRondas)
            }
            startActivity(intent)
            finish()
        }
    }
}
