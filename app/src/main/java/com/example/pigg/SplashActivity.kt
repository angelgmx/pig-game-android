package com.example.pigg

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DURATION: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ocultar la barra de acción
        supportActionBar?.hide()

        // Temporizador para pasar a MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity()
        }, SPLASH_DURATION)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cerrar splash para que no vuelva atrás
    }
}
