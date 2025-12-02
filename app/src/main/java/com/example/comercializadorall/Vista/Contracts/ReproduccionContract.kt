package com.example.comercializadorall.Vista.Contracts

interface ReproducirContract {
    fun mostrarVideo(videoUri: String)
    fun mostrarError(mensaje: String)
}