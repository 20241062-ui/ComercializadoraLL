package com.example.comercializadorall.Modelo

data class clsProductos(
    val vchNo_Serie:String,
    val vchNombre: String = "",
    val vchMarca: String = "",
    val vchImagen: String = "",
    val floPrecioUnitario: Float = 0.0f,
    val intStock: Int = 0,
    val vchDescripcion: String = "",
    val vchCategoria: String = "",
    val vchCobertura: String = "",
    var intCantidadCarrito: Int = 0
)
