package com.example.comercializadorall.Presentador

import com.bumptech.glide.load.model.Model
import com.example.comercializadorall.Modelo.*
import com.example.comercializadorall.Modelo.ICarritoModel
import com.example.comercializadorall.Presentador.CarritoPresenter
import com.example.comercializadorall.Vista.Contracts.ICarritoView

class CarritoPresenter(
    private val view: ICarritoView,
    private val modelo: ICarritoModel
) : ICarritoPresenter {

    override fun agregarAlCarrito(producto: clsProductos) {
        if (modelo.estaSesionIniciada()) {
            modelo.agregarProducto(producto)
            view.mostrarMensaje("Producto agregado al carrito")
        } else {
            view.mostrarMensaje("Debes iniciar sesión para agregar productos al carrito.")
        }
    }
    fun eliminarProducto(producto: clsProductos, position: Int) {
        modelo.eliminarProducto(producto)
        cargarCarrito()
    }

    override fun cargarCarrito() {
        val lista = modelo.obtenerCarrito()
        view.mostrarCarrito(lista)
    }
}