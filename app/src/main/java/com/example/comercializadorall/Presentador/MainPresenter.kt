package com.example.comercializadorall.Presentador

import android.util.Log
import com.example.comercializadorall.Modelo.clsProductos // Modelo actualizado
import com.example.comercializadorall.Vista.Contracts.MainContract // Contrato actualizado
import com.example.comercializadorall.Modelo.ifaceApiService // Interfaz de servicio actualizada
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class MainPresenter(private val view: MainContract) {

    private val apiService: ifaceApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://comercializadorall.grupoctic.com/ComercializadoraLL/API/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun obtenerProductos() {
        apiService.obtenerProductos().enqueue(object : Callback<List<clsProductos>> {

            override fun onResponse(call: Call<List<clsProductos>>, response: Response<List<clsProductos>>) {

                if (response.isSuccessful) {

                    response.body()?.let { productos: List<clsProductos> ->
                        view.mostrarProductos(productos)

                    } ?: run {
                        Log.e("API_DIAG", "Respuesta 200 pero cuerpo nulo. URL: ${call.request().url}")
                        view.mostrarError("Error de datos: El servidor respondió OK pero sin contenido.")
                    }

                } else {
                    val errorMsg = "HTTP Error ${response.code()}: ${response.message()}"
                    Log.e("API_DIAG", errorMsg)
                    view.mostrarError("Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) {
                val fullError = t.message ?: "Mensaje de error nulo"

                Log.e("API_DIAG", "Fallo de conexión o deserialización: $fullError", t)

                if (fullError.contains("malformed JSON", ignoreCase = true)) {
                    view.mostrarError("Error: JSON malformado. Revisa tu PHP.")
                } else {
                    view.mostrarError("Error de red: ${fullError}")
                }
            }
        })
    }
}
