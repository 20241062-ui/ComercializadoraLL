package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.ReproducirModel
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

    fun obtenerProductos() { // Método renombrado
        apiService.obtenerProductos().enqueue(object : Callback<List<clsProductos>> { // Tipos actualizados
            override fun onResponse(call: Call<List<clsProductos>>, response: Response<List<clsProductos>>) {
                if (response.isSuccessful) {
                    response.body()?.let { productos ->
                        view.mostrarProductos(productos) // Método de vista actualizado
                    } ?: run {
                        view.mostrarError("Error: ${response.message()}")
                    }
                } else {
                    view.mostrarError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) { // Tipos actualizados
                view.mostrarError("Error: ${t.message}")
            }
        })
    }
}