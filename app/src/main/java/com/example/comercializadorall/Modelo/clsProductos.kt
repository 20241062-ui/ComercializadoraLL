package com.example.comercializadorall.Modelo

data class clsProductos(
    val vchNo_Serie:String,
    val vchMarca: String,
    val vchCategoria:String,
    val vchCobertura:String,
    val vchNombre:String,
    val vchDescripcion:String,
    val Estado:Int,
    val floPrecioUnitario:Float,
    val intStock:Int,
    val floPrecioCompra:Float,
    val vchImagen:String
)
