package com.example.comercializadorall.Presentador

class RegistroPresenter {
    private val vista: RegistroContrac,
    private val model: RegistroModel) {
        fun registrarUsuario(nombreUsuario: String, email: String, password: String) {
            model.registrarUsuario(nombreUsuario, email, password, object : RegistroModel.OnRegistroListener {
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