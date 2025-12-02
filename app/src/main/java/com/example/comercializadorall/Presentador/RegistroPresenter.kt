package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.RegistroModel
import com.example.comercializadorall.Vista.Contracts.RegistroContract // Contrato actualizado

class RegistroPresenter (private val vista: RegistroContract,private val model: RegistroModel) {

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