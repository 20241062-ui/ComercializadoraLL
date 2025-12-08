package com.example.comercializadorall.Vista.Contracts

import com.example.comercializadorall.Modelo.clsInformacion

interface EmpresaContract {

    interface View {
        fun mostrarInformacion(mision: clsInformacion, vision: clsInformacion)
        fun mostrarError(mensaje: String)
    }

    // Lo que el Presenter puede hacer
    interface Presenter {
        fun cargarDatos()
    }
}