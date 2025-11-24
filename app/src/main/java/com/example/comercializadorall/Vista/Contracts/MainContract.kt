package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Presentador.ProductoVista

interface MainContract { // Renombrado
    fun mostrarProductos(productos: List<ProductoVista>) // Renombrado y tipo actualizado
    fun mostrarError(mensaje: String)

}