package com.example.comercializadorall.Modelo // Paquete actualizado

class ReproducirModel {
    // El método ahora maneja un 'nombreProducto' y apunta a la carpeta de videos de productos
    fun obtenerUrlVideo(nombreProducto: String): String {

        // URL Base actualizada a Productos/videos/
        return "https://javier.grupoctic.com/Productos/videos/$nombreProducto"
    }
}