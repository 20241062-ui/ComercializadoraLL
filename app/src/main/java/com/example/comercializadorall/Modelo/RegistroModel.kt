package com.example.comercializadorall.Modelo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroModel {
    interface OnRegistroListener {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

    fun registrarUsuario(nombreUsuario: String, email: String, password: String, listener: OnRegistroListener) {
        apiService.registrarUsuario("registrar", nombreUsuario, email, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            if (datos.isNotEmpty() && datos[0].Estado == "True") {
                                listener.onSuccess(datos[0].Salida)
                            } else {
                                listener.onFailure(datos[0].Salida)
                            }
                        } ?: listener.onFailure("Respuesta vacía o en formato incorrecto")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        listener.onFailure("Error en la respuesta del servidor: $errorBody")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    listener.onFailure("Error en la conexión: ${t.message}")
                }
            })
    }
}