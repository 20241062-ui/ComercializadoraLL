package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Modelo.DetalleModel

interface DetalleContract { // Renombrado
    fun mostrarProducto(producto: DetalleModel) // Renombrado y tipo actualizado
    fun reproducirVideo(videoUrl: String)
}