package com.example.pigg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pigg.databinding.ActivityConfigBinding

/**
 * OBJETIVO:
 * Es la primera pantalla (Activity) que ve el usuario. Su única responsabilidad
 * es permitirle configurar la partida:
 *   1. Elegir el número de jugadores (2, 3 o 4).
 *   2. Especificar el número de rondas (de 1 a 10).
 * Una vez que el usuario ha hecho una selección válida, esta pantalla navega
 * automáticamente a `ElegirJugadores`, pasándole los datos seleccionados.
 */
class ConfigActivity : AppCompatActivity() {

    /**
     * `binding`: Es una variable para acceder a las vistas del layout (botones, spinners, etc.)
     * de una forma moderna y segura, gracias a "View Binding".
     *
     * `lateinit`: Significa que la inicializaremos más tarde (en `onCreate`), evitando
     * que sea nula (`null`) y ahorrándonos comprobaciones.
     */
    private lateinit var binding: ActivityConfigBinding


    /**
     * `onCreate`: Es el "punto de entrada" de la actividad. Se ejecuta una sola vez
     * cuando la pantalla es creada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Llamada obligatoria al método de la clase padre

        // 1. INFLAR LA VISTA CON VIEW BINDING
        // Esto crea todos los objetos de la vista a partir de tu XML (activity_config.xml)
        // y nos permite acceder a ellos a través de la variable `binding`.
        binding = ActivityConfigBinding.inflate(layoutInflater)

        // 2. ESTABLECER LA VISTA
        // Le decimos a la actividad que su contenido visual será el que hemos inflado.
        setContentView(binding.root)

        // 3. INICIAR LA LÓGICA DE LA PANTALLA
        // Llamamos a la función que se encarga de configurar el Spinner y sus eventos.
        configurarSpinner()
    }


    /**
     * `configurarSpinner`: Prepara el desplegable (Spinner) de los jugadores.
     * Define sus opciones, cómo se ven y qué hacer cuando el usuario elige una.
     */
    private fun configurarSpinner() {
        // 1. DATOS: Creamos la lista de opciones que se mostrarán en el Spinner.
        val opciones = arrayOf(
            "Selecciona jugadores", // Posición 0: Un texto de ayuda, no una opción real.
            "2 Jugadores",          // Posición 1
            "3 Jugadores",          // Posición 2
            "4 Jugadores"           // Posición 3
        )

        // 2. ADAPTADOR: Es el "puente" entre nuestros datos (`opciones`) y la interfaz (`Spinner`).
        // Le dice al Spinner cómo mostrar cada uno de los elementos de la lista.
        val adapter = ArrayAdapter(
            this,                               // Contexto: La actividad actual.
            android.R.layout.simple_spinner_item, // Layout de Android para el ítem cuando está cerrado.
            opciones                            // La lista de datos a mostrar.
        )
        // Especificamos un layout diferente para cuando el desplegable está abierto (más espaciado).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 3. ASIGNACIÓN: Conectamos el Spinner de nuestro layout con el adaptador que hemos creado.
        binding.spinner.adapter = adapter

        // 4. LISTENER: Definimos qué debe ocurrir cuando el usuario selecciona un ítem.
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Comprobamos que el usuario no haya seleccionado la primera opción ("Selecciona jugadores").
                if (position > 0) {
                    // Extraemos el texto de la opción seleccionada.
                    val seleccionado = opciones[position]
                    binding.texNumJugadores.text = "Has elegido $seleccionado"

                    // Intentamos validar el número de rondas introducido.
                    val texRondas = binding.numrondas.text.toString()
                    val numRondas = validarRondas(texRondas)

                    // Si la validación es exitosa (no es null), procedemos a navegar.
                    if (numRondas != null) {
                        // El número real de jugadores es la posición + 1 (ej: posición 1 -> 2 jugadores)
                        val numJugadores = position + 1
                        navegarSeleccionJugadores(numJugadores, numRondas)
                    }
                } else {
                    // Si el usuario vuelve a la posición 0, mostramos un mensaje por defecto.
                    binding.texNumJugadores.text = "Selecciona una opción"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Este método se podría usar si el adaptador se queda vacío, pero no es nuestro caso.
            }
        }
    }


    /**
     * `validarRondas`: Comprueba que el texto introducido para las rondas sea un número válido
     * entre 1 y 10. Devuelve el número si es correcto, o `null` si hay un error.
     */
    private fun validarRondas(texto: String): Int? {
        if (texto.isEmpty()) {
            mostrarMensaje("Por favor, ingresa el número de rondas")
            return null
        }
        val numero = texto.toIntOrNull() // Convierte el texto a Int, o devuelve null si no es un número.
        if (numero == null) {
            mostrarMensaje("Por favor, ingresa un número válido para las rondas")
            return null
        }
        if (numero < 1 || numero > 10) {
            mostrarMensaje("El número de rondas debe estar entre 1 y 10")
            return null
        }
        return numero // Si todas las validaciones pasan, devuelve el número.
    }

    /**
     * `mostrarMensaje`: Una función simple para mostrar notificaciones cortas (Toast).
     */
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    /**
     * `resetearSpinner`: Vuelve a poner el Spinner en su posición inicial (la 0).
     * Es útil para cuando el usuario vuelve a esta pantalla desde la siguiente.
     */
    private fun resetearSpinner() {
        binding.spinner.setSelection(0)
    }

    /**
     * `navegarSeleccionJugadores`: Se encarga de iniciar la siguiente actividad (`ElegirJugadores`)
     * y de pasarle los datos de la partida.
     */
    private fun navegarSeleccionJugadores(numJugadores: Int, numRondas: Int) {
        // 1. INTENT: Creamos un "intento" o "intención" de ir a la pantalla `ElegirJugadores`.
        val intentParaElegir = Intent(this, ElegirJugadores::class.java)

        // 2. EXTRAS: Añadimos los datos de la partida al intent. Se guardan como clave-valor.
        intentParaElegir.putExtra("NUM_JUGADORES", numJugadores)
        intentParaElegir.putExtra("NUM_RONDAS", numRondas)

        // 3. START: Ejecutamos el intent, lo que hace que Android abra la nueva actividad.
        startActivity(intentParaElegir)
        finish()

        // 4. RESET: Reseteamos el Spinner para que, si el usuario vuelve atrás, la interfaz esté limpia.
        resetearSpinner()
    }
}
