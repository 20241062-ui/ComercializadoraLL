package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Modelo.ifaceApiService // Interfaz actualizada
import com.example.comercializadorall.Vista.clsDatosRespuesta // Clase de respuesta
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context

class LoginModel (private val context: Context){

    private val apiService: ifaceApiService

    // 🔑 Preferencias y Claves (Deben coincidir con CarritoModel)
    private val sessionPrefs = context.getSharedPreferences("SESION_PREF", Context.MODE_PRIVATE)
    private val sessionKey = "usuario_logueado" // Clave para el ID del usuario

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://comercializadorall.grupoctic.com/ComercializadoraLL/API/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ifaceApiService::class.java)
    }

    // Método para guardar los datos de la sesión de forma persistente
    fun guardarSesionActiva(idUsuario: String) {
        sessionPrefs.edit().apply {
            putString(sessionKey, idUsuario)
            apply()
        }
    }

    // Lógica para iniciar sesión
    fun iniciarSesion(correo: String, password: String, callback: (List<clsDatosRespuesta>?, String?) -> Unit) {
        apiService.iniciarSesion("login", correo, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        val datosRespuesta = response.body()

                        if (datosRespuesta != null && datosRespuesta.firstOrNull()?.Estado == "Correcto") {
                            val userData = datosRespuesta.firstOrNull()
                            if (userData != null) {
                                // 1. Extraer los datos necesarios (Asumiendo que 'user_id' y 'nombreUsuario' existen en clsDatosRespuesta)
                                val userId = userData.user_id.toString()

                                // 2. 🚨 Llamada a la función de guardado
                                guardarSesionActiva(userId)
                            }
                        }
                        callback(datosRespuesta, null)
                    } else {
                        callback(null, "Error en la respuesta del servidor: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    callback(null, "Error en la conexión: ${t.message}")
                }
            })
    }
}