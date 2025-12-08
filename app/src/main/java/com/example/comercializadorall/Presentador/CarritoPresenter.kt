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
        // 1. VERIFICAR EL ESTADO DE LA SESIÓN
        if (modelo.estaSesionIniciada()) {
            // Si la sesión está iniciada, procede a agregar el producto
            modelo.agregarProducto(producto)
            view.mostrarMensaje("Producto agregado al carrito")
        } else {
            // 2. MOSTRAR MENSAJE DE ERROR (Y quizás navegar a Login)
            view.mostrarMensaje("Debes iniciar sesión para agregar productos al carrito.")
            // 🚨 OPCIONAL: Si tu ICarritoView tiene un método para navegar a Login, úsalo aquí.
            // view.navegarALogin()
        }
    }
    fun eliminarProducto(producto: clsProductos, position: Int) {
        modelo.eliminarProducto(producto)
        cargarCarrito()
    }

    override fun cargarCarrito() {
        // Podrías añadir la verificación aquí también, si no quieres mostrar el carrito
        // a usuarios no logueados, pero generalmente se muestra vacío o con mensaje.
        val lista = modelo.obtenerCarrito()
        view.mostrarCarrito(lista)
    }
}