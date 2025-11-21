package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Modelo.clsProductos

interface MainContract { // Renombrado
    fun mostrarProductos(productos: List<clsProductos>) // Renombrado y tipo actualizado
    fun mostrarError(mensaje: String)
}