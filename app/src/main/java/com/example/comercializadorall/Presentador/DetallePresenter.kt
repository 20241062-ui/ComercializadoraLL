package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.DetalleModel // Modelo de detalle actualizado
import com.example.comercializadorall.Vista.Contracts.DetalleContract // Contrato actualizado

class DetalleProductoPresenter(private val vista: DetalleContract) { // Clase renombrada y contrato actualizado

    fun cargarDatos(intentExtras: Map<String, String?>) {
        // Mapeo de claves de Intent actualizadas de 'pelicula_' a 'producto_'
        val producto = DetalleModel(
            nombre = intentExtras["producto_nombre"],
            descripcion = intentExtras["producto_descripcion"],
            especificaciones = intentExtras["producto_sinopsis"], // Clave asumida: sinopsis -> especificaciones
            imagen = intentExtras["producto_imagen"],
            precio = intentExtras["producto_precio"]?.toFloatOrNull(), // Nuevo campo
            video = intentExtras["producto_video"]
        )
        // Método de vista actualizado
        vista.mostrarProducto(producto)
    }

    fun onReproducirClicked(videoUrl: String?) {
        videoUrl?.let {
            vista.reproducirVideo(it)
        }
    }
}