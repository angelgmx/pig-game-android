package com.example.pigg

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pigg.databinding.ActivityResultsBinding

class ResultadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding

    private var ganador : String? = null
    private var tablero : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recibirResultadosJuego()
        resultadosFinales()
    }

    private fun recibirResultadosJuego(){
        val bundle = intent.extras
        //recibir datos de intent
        ganador = bundle?.getString("GANADOR")
        tablero = bundle?.getString("TABLERO")

    }

    private fun resultadosFinales(){
        if (ganador != null) {
            binding.textoGanador.text = "¡EL GANADOR ES $ganador !"
        } else {
            binding.textoGanador.text = "HA HABIDO UN EMPATE"
        }

        val resultadosFinales = tablero
        binding.txtPuntuacionesFinales.text = tablero
    }

}