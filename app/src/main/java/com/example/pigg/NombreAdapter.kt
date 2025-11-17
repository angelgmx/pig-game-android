package com.example.pigg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pigg.databinding.ItemJugadoresBinding

class NombreAdapter(
    private var listaNombres: List<String>,
    private val onNombreSeleccionado: (String) -> Unit
) : RecyclerView.Adapter<NombreAdapter.ViewHolder>() {

    /**
     * ViewHolder es el contenedor de vista del elemento individual de la lista. Cuando se crea el contenedor no tiene datos asociados
     * La RecyclerView vincula los datos entre el ViewHolder y el recyclerview
     */
    class ViewHolder(val bindig: ItemJugadoresBinding) : RecyclerView.ViewHolder(bindig.root)

    /**
     * Este metodo crea e inicializa el ViewHolder y la View asociada, pero no se completa el contenido de la vista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bindig = ItemJugadoresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bindig)
    }

    /**
     * Conecta los datos (un nombre) con las vistas del ViewHolder usando el objeto de binding.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Cuando el usuario pulsa una posición de la lista del recycler se guarda un nombre
        val nombre = listaNombres[position]
        // Establecer el texto del nombre en el TextView
        holder.bindig.txtNombre.text = nombre
        // configurar un click listener para que cuando se haga click llame a onNombreSeleccionado del Activiy
        holder.bindig.root.setOnClickListener {
            onNombreSeleccionado(nombre)  // Esto ejecuta el callback del adaptador
        }
    }

    /**
     * Devuelve la cantidad total de elementos en la lista de datos.
     */
    override fun getItemCount() = listaNombres.size

    /**
     * Actualiza la lista de nombres que muestra el adaptador y notifica al RecyclerView
     * que debe redibujarse para reflejar los cambios.
     * @param nuevosNombres La nueva lista de nombres a mostrar.
     */
//    fun actualizarNombres(nuevosNombres: List<String>) {
//        this.listaNombres = nuevosNombres
//    }
}