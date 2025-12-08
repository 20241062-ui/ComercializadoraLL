package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.clsProductos




    fun clsProductos.toProductoVista(): ProductoVista {
        val precioString = "Precio: $${String.format("%.2f", this.floPrecioUnitario)}"

        val fullImageUrl = "https://comercializadorall.grupoctic.com/ComercializadoraLL/Recursos/" + this.vchImagen

        return ProductoVista(
            idSerie = this.vchNo_Serie,
            nombreDisplay = this.vchNombre,
            precioFormatted = precioString,
            stockDisponible = this.intStock,
            urlImagenCompleta = fullImageUrl
        )
    }
