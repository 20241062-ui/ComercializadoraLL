package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Modelo.clsProductos

interface ICarritoView {
    fun mostrarCarrito(lista: MutableList<clsProductos>)
    fun mostrarMensaje(msg: String)
    fun navegarALogin()
}