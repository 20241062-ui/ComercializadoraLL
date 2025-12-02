package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Vista.Contracts.ReproducirContract // Contrato actualizado
import com.example.comercializadorall.Modelo.ReproducirModel


class ReproducirPresenter(val vista: ReproducirContract) { // Clase renombrada
    private val modelo = ReproducirModel()

    fun cargarVideo(nombreArchivoVideo: String) { // Renombrado
        if (nombreArchivoVideo.isEmpty()) {
            vista.mostrarError("Nombre de archivo de video vacío")
        } else {
            val url = modelo.obtenerUrlVideo(nombreArchivoVideo)
            vista.mostrarVideo(url)
        }
    }
}