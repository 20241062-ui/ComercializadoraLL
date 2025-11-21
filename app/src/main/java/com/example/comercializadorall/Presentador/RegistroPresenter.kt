package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.RegistroModel
import com.example.comercializadorall.Vista.Contracts.RegistroContract // Contrato actualizado

class RegistrPresenter (
    private val vista: RegistroContract, // Contrato actualizado
    private val model: RegistroModel) {
    // Parámetros actualizados para incluir apellido y el nombre correcto del campo (correo)
    fun registrarUsuario(nombre: String, apellido: String, correo: String, password: String) {
        val rolFijo = "Cliente"
        model.registrarUsuario(nombre, apellido, correo, password,rolFijo, object : RegistroModel.OnRegistroListener {
            override fun onSuccess(message: String) {
                vista.mostrarMensaje(message)
                vista.registroExitoso()
            }

            override fun onFailure(message: String) {
                vista.mostrarMensaje(message)
            }
        })
    }
}