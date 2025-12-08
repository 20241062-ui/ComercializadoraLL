package com.example.comercializadorall.Modelo

interface ICarritoModel {
    fun agregarProducto(producto: clsProductos)
    fun obtenerCarrito(): MutableList<clsProductos>
    fun guardarCarrito(lista: MutableList<clsProductos>)
    fun limpiarCarrito()
    fun estaSesionIniciada(): Boolean
    fun eliminarProducto(producto: clsProductos)
}