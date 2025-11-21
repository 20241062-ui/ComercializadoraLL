package com.example.comercializadorall.Vista.Contracts

interface ReproducirContract { // Renombrado
    fun mostrarVideo(videoUri: String)
    fun mostrarError(mensaje: String)
}