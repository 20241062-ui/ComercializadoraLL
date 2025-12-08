package com.example.comercializadorall.Presentador

import android.util.Log
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Modelo.ifaceApiService
import com.example.comercializadorall.Vista.Contracts.CategoriasContract
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object RetrofitClient {
    private const val BASE_URL = "https://comercializadorall.grupoctic.com/ComercializadoraLL/API/"

    val instance: ifaceApiService by lazy {

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        retrofit.create(ifaceApiService::class.java)
    }
}

class CategoriasRepository(private val apiService: ifaceApiService) : CategoriasContract.Model {
    override fun obtenerCategoriasIniciales(): Call<List<clsProductos>> {
        return apiService.obtenerProductos()
    }
    override fun buscarProductosPorQuery(query: String): Call<List<clsProductos>> {
        return apiService.buscarProductosPorQuery(query)
    }
    override fun obtenerProductoPorCodigo(codigoQR: String): Call<clsProductos> {
        return apiService.obtenerProductoPorCodigo(codigoQR)
    }
}

class CategoriasPresenter(private var view: CategoriasContract.View?, private val repository: CategoriasRepository = CategoriasRepository(RetrofitClient.instance)
) : CategoriasContract.Presenter {

    override fun iniciar() {
        view?.mostrarCargando(true)

        repository.obtenerCategoriasIniciales().enqueue(object : Callback<List<clsProductos>> {
            override fun onResponse(call: Call<List<clsProductos>>, response: Response<List<clsProductos>>) {
                view?.mostrarCargando(false)
                if (response.isSuccessful && response.body() != null) {
                    view?.mostrarCategorias(response.body()!!)
                } else {
                    view?.mostrarMensajeError("Error: No hay categorías disponibles.")
                }
            }
            override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) {
                view?.mostrarCargando(false)
                view?.mostrarMensajeError("Error de red: ${t.message}")
            }
        })
    }

    override fun buscarProductos(query: String) {
        val queryLimpia = query.trim()
        if (queryLimpia.isBlank()) {
            view?.mostrarMensajeError("Introduce un término de búsqueda.")
            return
        }

        view?.mostrarCargando(true)

        repository.buscarProductosPorQuery(queryLimpia).enqueue(object : Callback<List<clsProductos>> {
            override fun onResponse(call: Call<List<clsProductos>>, response: Response<List<clsProductos>>) {
                view?.mostrarCargando(false)
                if (response.isSuccessful && response.body() != null) {
                    val resultados = response.body()!!
                    if (resultados.isNotEmpty()) {
                        view?.mostrarResultadosBusqueda(resultados)
                    } else {
                        view?.mostrarMensajeError("No se encontraron productos para \"$query\".")
                    }
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.message()

                    if (errorCode == 200) {
                        view?.mostrarMensajeError("Error 200: El servidor devolvió una respuesta vacía o malformada.")
                    } else {
                        view?.mostrarMensajeError("Error. HTTP $errorCode: $errorMessage")
                    }
                }
            }
            override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) {
                view?.mostrarCargando(false)
                view?.mostrarMensajeError("Error: ${t.message}")
            }
        })
    }

    override fun escanearQR(codigoQR: String) {

        val codigoLimpio = codigoQR.trim()

        if (codigoLimpio.isBlank()) {
            view?.mostrarMensajeError("El código QR no es válido.")
            return
        }

        view?.mostrarCargando(true)

        repository.obtenerProductoPorCodigo(codigoLimpio).enqueue(object : Callback<clsProductos> {

            override fun onResponse(call: Call<clsProductos>, response: Response<clsProductos>) {
                view?.mostrarCargando(false)
                val producto = response.body()

                if (response.isSuccessful && producto != null) {
                    view?.navegarADetalleProducto(producto)
                } else if (response.code() == 404) {
                    view?.mostrarMensajeError("Código QR no válido o producto no encontrado (404).")
                } else {
                    view?.mostrarMensajeError("Error al procesar el código QR.")
                }
            }
            override fun onFailure(call: Call<clsProductos>, t: Throwable) {
                view?.mostrarCargando(false)
                view?.mostrarMensajeError("Error de red al escanear: ${t.message}")
            }
        })
    }

    override fun detener() {
        view = null
    }
}