package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Modelo.clsProductos
import retrofit2.Call

interface CategoriasContract {
    interface View {
        fun mostrarCategorias(categorias: List<clsProductos>)
        fun mostrarResultadosBusqueda(productos: List<clsProductos>)
        fun mostrarMensajeError(mensaje: String)
        fun navegarADetalleProducto(producto: clsProductos) // Navegación con el objeto completo
        fun mostrarCargando(isLoading: Boolean)
    }

    // MARK: Interface para el Presenter
    interface Presenter {
        fun iniciar()
        fun buscarProductos(query: String)
        fun escanearQR(codigoQR: String)
        fun detener()
    }
    interface Model {
        fun obtenerCategoriasIniciales(): Call<List<clsProductos>>
        fun buscarProductosPorQuery(query: String): Call<List<clsProductos>>
        fun obtenerProductoPorCodigo(codigoQR: String): Call<clsProductos>
    }
}