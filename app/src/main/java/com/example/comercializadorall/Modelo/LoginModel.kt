package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Vista.clsDatosRespuesta
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.SharedPreferences

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

    fun guardarSesionActiva(idUsuario: String) {
        sessionPrefs.edit().apply {
            putString(sessionKey, idUsuario)
            apply()
        }
    }

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
                            if (userData?.user_id != null) {
                                val userId = userData.user_id.toString()
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