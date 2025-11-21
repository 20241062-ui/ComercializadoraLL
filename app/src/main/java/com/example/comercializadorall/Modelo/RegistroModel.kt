package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Vista.clsDatosRespuesta
import com.example.comercializadorall.Modelo.ifaceApiService // Interfaz actualizada
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// El constructor ahora espera la interfaz actualizada
class RegistroModel(private val apiService: ifaceApiService) {

    interface OnRegistroListener {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

    // Parámetros actualizados para incluir apellido y los nombres de campo de la API (vchnombre, vchcorreo)
    fun registrarUsuario(nombre: String, apellido: String, correo: String, password: String,rolFijo:String, listener: OnRegistroListener) {
        apiService.registrarUsuario("registrar", nombre, apellido, correo, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            // El API de registro devuelve 'true' como String
                            if (datos.isNotEmpty() && datos[0].Estado.equals("true", ignoreCase = true)) {
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