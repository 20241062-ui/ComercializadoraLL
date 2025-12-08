package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Vista.clsDatosRespuesta
import com.example.comercializadorall.Modelo.ifaceApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Patterns // Import necesario para la validación de email

class RegistroModel(private val apiService: ifaceApiService) {

    interface OnRegistroListener {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

    fun registrarUsuario(nombre: String, apellido: String, correo: String, password: String, rolFijo: String, listener: OnRegistroListener) {


        if (nombre.isBlank() || apellido.isBlank() || correo.isBlank() || password.isBlank()) {
            listener.onFailure("Todos los campos (nombre, apellido, correo y contraseña) son obligatorios.")
            return
        }

        if (nombre.contains(Regex("[0-9]")) || apellido.contains(Regex("[0-9]"))) {
            listener.onFailure("El nombre y el apellido no deben contener números.")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            listener.onFailure("El formato del correo electrónico es inválido. Por favor, revísalo.")
            return
        }

        if (password.length < 8) {
            listener.onFailure("La contraseña debe tener al menos 8 caracteres para ser segura.")
            return
        }

        apiService.registrarUsuario("registrar", nombre, apellido, correo, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            if (datos.isNotEmpty() && datos[0].Estado.equals("true", ignoreCase = true)) {
                                listener.onSuccess(datos[0].Salida)
                            } else {
                                listener.onFailure(datos[0].Salida)
                            }
                        } ?: listener.onFailure("Respuesta vacía o en formato incorrecto del servidor.")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        listener.onFailure("Error ${response.code()} en la respuesta del servidor: $errorBody")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    listener.onFailure("Error de conexión: Asegúrate de que tienes conexión a internet. Mensaje: ${t.message}")
                }
            })
    }
}