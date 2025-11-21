package com.example.comercializadorall.Modelo

data class clsProductos(
    val vchNo_Serie:Int,
    val intid_Marca:Int,
    val intid_Categoria:Int,
    val intid_Cobertura:Int,
    val vchNombre:String,
    val vchDescripcion:String,
    val Estado:Int,
    val floPrecioUnitario:Float,
    val intStock:Int,
    val floPrecioCompra:Float,
    val vchImagen:Int
)
