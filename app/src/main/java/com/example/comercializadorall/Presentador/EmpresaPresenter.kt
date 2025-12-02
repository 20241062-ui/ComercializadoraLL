package com.example.comercializadorall.Presentador

// src/main/java/com/example/comercializadorall/Presentador/EmpresaPresenter.kt


import android.util.Log
import com.example.comercializadorall.Modelo.clsInformacion
import com.example.comercializadorall.Vista.Contracts.EmpresaContract
import com.example.comercializadorall.Modelo.ifaceApiService // <--- Usamos tu interfaz única
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*class EmpresaPresenter(private val view: EmpresaContract.View) : EmpresaContract.Presenter {

    private val apiService: ifaceApiService

    init {
        // *** VERIFICA ESTA URL BASE ***
        // Debe ser la carpeta que contiene tu archivo apiInformacionEmpresa.php
        val retrofit = Retrofit.Builder()
            .baseUrl("https://comercializadorall.grupoctic.com/ComercializadoraLL/API/") // <-- Usar la URL base de tu Presenter principal
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        apiService = retrofit.create(ifaceApiService::class.java)
    }

    override fun obtenerInformacionEmpresa() {
        // Llama al método que acabamos de agregar a tu interfaz
        apiService.obtenerInformacionEmpresa().enqueue(object : Callback<List<clsInformacion>> {

            // ... (Resto del código de onResponse y onFailure, que no necesita cambios) ...

            override fun onResponse(call: Call<List<clsInformacion>>, response: Response<List<clsInformacion>>) {
                // Lógica de filtrado: encuentra Misión y Visión con Estado = 1
                if (response.isSuccessful) {
                    val listaInformacion: List<clsInformacion>? = response.body()

                    if (listaInformacion != null && listaInformacion.isNotEmpty()) {
                        val activos = listaInformacion.filter { it.Estado == 1 }

                        // Asegúrate que 'Misión' y 'Visión' sean exactamente como están en tu campo vchtitulo
                        val mision = activos.find { it.vchtitulo.equals("Misión", ignoreCase = true) }?.vchcontenido
                        val vision = activos.find { it.vchtitulo.equals("Visión", ignoreCase = true) }?.vchcontenido

                        if (mision != null && vision != null) {
                            view.mostrarInformacion(mision, vision)
                        } else {
                            view.mostrarError("Error: No se encontró la Misión o Visión activa.")
                        }
                    } else {
                        view.mostrarError("Error: El servidor no devolvió información.")
                    }
                } else {
                    view.mostrarError("Error al obtener datos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<clsInformacion>>, t: Throwable) {
                Log.e("API_EMPRESA", "Fallo de conexión: ${t.message}")
                view.mostrarError("Error de red: No se pudo conectar con el servidor.")
            }
        })
    }
}*/