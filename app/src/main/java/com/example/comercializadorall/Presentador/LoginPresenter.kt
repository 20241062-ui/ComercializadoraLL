package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.Vista.Contracts.LoginContract // Contrato actualizado

class LoginPresenter(
    private val vista: LoginContract, private val model: LoginModel) {

    fun iniciarSesion(correo: String, password: String) {
        if (correo.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Debe llenar todos los campos")
            return
        }

        model.iniciarSesion(correo, password) { datos, error ->
            if (error != null) {
                vista.mostrarMensaje(error)
            } else if (datos != null && datos.firstOrNull()?.Estado == "Correcto") {
                vista.navegarAMain()
            } else {
                vista.mostrarMensaje("Usuario o contraseña incorrectos")
            }
        }
    }
}