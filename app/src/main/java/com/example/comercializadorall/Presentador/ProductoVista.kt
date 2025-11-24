package com.example.comercializadorall.Presentador

data class ProductoVista(
    val idSerie: String,
    val nombreDisplay: String,
    val precioFormatted: String,
    val stockDisponible: Int,
    val urlImagenCompleta: String
)