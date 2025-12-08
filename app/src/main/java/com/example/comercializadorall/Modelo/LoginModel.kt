package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Vista.clsDatosRespuesta // Clase de respuesta
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.SharedPreferences
import com.example.comercializadorall.Vista.AppConstants.PREFS_NAME
import com.example.comercializadorall.Vista.AppConstants.SESSION_KEY
import com.example.comercializadorall.Vista.Perfil

class LoginModel (private val sessionPrefs: SharedPreferences, private val sessionKey: String){

    private val apiService: ifaceApiService

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
    fun obtenerIdUsuarioActivo(): String? {
        // El segundo parámetro (null) es el valor por defecto si la clave no se encuentra.
        return sessionPrefs.getString(sessionKey, null)
    }
    fun cerrarSesion() {
        sessionPrefs.edit().apply {
            // Elimina solo la clave de sesión
            remove(sessionKey)
            // Si hay otras configuraciones que quieras borrar, usa clear() en su lugar.
            // clear()
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