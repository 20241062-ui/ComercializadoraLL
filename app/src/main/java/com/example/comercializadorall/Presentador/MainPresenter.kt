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




class MainPresenter(private val view: MainContract) { // Contrato actualizado

    private val apiService: ifaceApiService // Interfaz de servicio actualizada

    init {
        val retrofit = Retrofit.Builder()
            // URL base actualizada a Productos/api/
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
                        // Si la respuesta es 200 OK, pero el cuerpo (body) es nulo
                        Log.e("API_DIAG", "Respuesta 200 pero cuerpo nulo. URL: ${call.request().url}")
                        view.mostrarError("Error de datos: El servidor respondió OK pero sin contenido.")
                    }

                } else {
                    // Si la respuesta HTTP no es 200 (ej. 404, 500, 401)
                    val errorMsg = "HTTP Error ${response.code()}: ${response.message()}"
                    Log.e("API_DIAG", errorMsg)
                    view.mostrarError("Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) {
                // Falla de red, tiempo de espera (timeout) o error de deserialización (Gson)
                val fullError = t.message ?: "Mensaje de error nulo"

                Log.e("API_DIAG", "Fallo de conexión o deserialización: $fullError", t)

                // Si el error es el famoso "malformed JSON", imprimimos un mensaje específico:
                if (fullError.contains("malformed JSON", ignoreCase = true)) {
                    view.mostrarError("Error: JSON malformado. Revisa tu PHP.")
                } else {
                    view.mostrarError("Error de red: ${fullError}")
                }
            }
        })
    }
    /*fun buscarProductoPorCodigo(codigo: String) {
        view.mostrarCargando(true) // Asumo que tienes una función para mostrar carga

        // 🚨 PASO 1: Llama a la API con el código de producto (vchNo_Serie)
        apiService.obtenerProductoPorCodigo(codigo).enqueue(object : Callback<clsProductos> {

            override fun onResponse(call: Call<clsProductos>, response: Response<clsProductos>) {
                view.mostrarCargando(false)

                if (response.isSuccessful) {
                    val producto = response.body()

                    if (producto != null) {
                        // 💡 ÉXITO: El Presenter le indica a la View (MainActivity) que navegue
                        view.mostrarDetalleProductoDirecto(producto)
                    } else {
                        view.mostrarError("Producto con código $codigo no encontrado.")
                    }
                } else {
                    // Error 404, 500, etc.
                    Log.e("API_SEARCH", "Error ${response.code()}: ${response.message()}")
                    view.mostrarError("Error ${response.code()} al buscar el producto.")
                }
            }

            override fun onFailure(call: Call<clsProductos>, t: Throwable) {
                view.mostrarCargando(false)
                Log.e("API_SEARCH", "Fallo de conexión o deserialización: ${t.message}", t)
                view.mostrarError("Error de red al buscar el producto.")
            }
        })
    }*/
}
