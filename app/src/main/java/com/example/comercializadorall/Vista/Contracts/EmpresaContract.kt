package com.example.comercializadorall.Vista.Contracts

interface EmpresaContract {
    fun mostrarInformacion(mision: String, vision: String)
    fun mostrarError(mensaje: String)
}