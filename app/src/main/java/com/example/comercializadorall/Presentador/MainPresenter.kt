package com.example.comercializadorall.Presentador

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainPresenter {
    private val apiService: ifaceApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://javier.grupoctic.com/Peliculas/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        apiService = retrofit.create(ifaceApiService::class.java)
    }

    fun obtenerPeliculas() {
        apiService.obtenerPeliculas().enqueue(object : Callback<List<clsPeliculas>> {
            override fun onResponse(call: Call<List<clsPeliculas>>, response: Response<List<clsPeliculas>>) {
                if (response.isSuccessful) {
                    response.body()?.let { peliculas ->
                        view.mostrarPeliculas(peliculas)
                    } ?: run {
                        view.mostrarError("Error: ${response.message()}")
                    }
                } else {
                    view.mostrarError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<clsPeliculas>>, t: Throwable) {
                view.mostrarError("Error: ${t.message}")
            }
        })
    }
}