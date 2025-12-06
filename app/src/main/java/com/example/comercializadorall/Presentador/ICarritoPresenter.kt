package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.clsProductos

interface ICarritoPresenter {
    fun agregarAlCarrito(producto: clsProductos)
    fun cargarCarrito()
}