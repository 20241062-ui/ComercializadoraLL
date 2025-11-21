package com.example.comercializadorall.Presentador

class DetallePresenter {
    fun cargarDatos(intentExtras: Map<String, String?>) {
        val pelicula = DetalleModel(
            nombre = intentExtras["pelicula_nombre"],
            descripcion = intentExtras["pelicula_descripcion"],
            sinopsis = intentExtras["pelicula_sinopsis"],
            imagen = intentExtras["pelicula_imagen"],
            video = intentExtras["pelicula_video"]
        )
        vista.mostrarPelicula(pelicula)
    }

    fun onReproducirClicked(videoUrl: String?) {
        videoUrl?.let {
            vista.reproducirVideo(it)
        }
    }
}