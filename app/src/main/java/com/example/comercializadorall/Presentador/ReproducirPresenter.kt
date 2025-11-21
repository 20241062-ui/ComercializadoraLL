package com.example.comercializadorall.Presentador

class ReproducirPresenter(val vista: ReproducirContrac) {
    private val modelo = ReproducirModel()

    fun cargarVideo(nombrePelicula: String) {
        if (nombrePelicula.isEmpty()) {
            vista.mostrarError("Nombre de película vacío")
        } else {
            val url = modelo.obtenerUrlVideo(nombrePelicula)
            vista.mostrarVideo(url)
        }
    }
}