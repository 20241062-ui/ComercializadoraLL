package com.example.comercializadorall.Modelo // Ubicación común para utilidades y servicios

import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Vista.clsDatosRespuesta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import android.util.Log

// Simulación de la URL constante (como referencia de que debe existir)
private object Constants {
    const val URL_BASE = "https://comercializadorall.grupoctic.com/ComercializadoraLL/API/"
}
    object TestApi {

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient()) // Uso de OkHttpClient sin configuración especial
            .build()

        // 2. Creación del servicio API (similar a tu ejemplo)
        private val ApiService = retrofit.create(ifaceApiService::class.java)

        // ---------------------------------------------------------------------
        // Funciones de Prueba
        // ---------------------------------------------------------------------

        /**
         * Prueba simple para obtener la lista completa de productos.
         * Corresponde a `ApiService.obtenerProductos()`
         */
        fun probarObtenerProductosSimple() {
            Log.d("PRUEBA_RETROFIT", "Iniciando prueba: Obtener Productos")

            ApiService.obtenerProductos().enqueue(object : Callback<List<clsProductos>> {
                override fun onResponse(
                    call: Call<List<clsProductos>>,
                    response: Response<List<clsProductos>>
                ) {
                    if (response.isSuccessful) {
                        val productos = response.body()
                        Log.d("PRUEBA_RETROFIT", "LISTADO_PRODUCTOS => Cantidad: ${productos?.size ?: 0}")
                        // Puedes añadir un log.v para ver el primer producto si quieres más detalle
                        // Log.v("PRUEBA_RETROFIT", "Primer Producto: ${productos?.firstOrNull()?.vchNombre}")
                    } else {
                        Log.e("PRUEBA_RETROFIT", "ERROR_HTTP => ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<clsProductos>>, t: Throwable) {
                    Log.e("PRUEBA_RETROFIT", "ERROR_CONEXION => ${t.message}")
                }
            })
        }

        /**
         * Prueba simple para buscar un producto por código.
         * Corresponde a `ApiService.obtenerProductoPorCodigo(codigo)`
         */
        fun probarObtenerProductoPorCodigo(codigo: String) {
            Log.d("PRUEBA_RETROFIT", "Iniciando prueba: Obtener Producto por Código: $codigo")

            ApiService.obtenerProductoPorCodigo(codigo).enqueue(object : Callback<clsProductos> {
                override fun onResponse(
                    call: Call<clsProductos>,
                    response: Response<clsProductos>
                ) {
                    if (response.isSuccessful) {
                        val producto = response.body()
                        Log.d("PRUEBA_RETROFIT", "PRODUCTO_ENCONTRADO => ${producto?.vchNombre ?: "Producto no encontrado"}")
                    } else {
                        Log.e("PRUEBA_RETROFIT", "ERROR_HTTP => ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<clsProductos>, t: Throwable) {
                    Log.e("PRUEBA_RETROFIT", "ERROR_CONEXION => ${t.message}")
                }
            })
        }

        /**
         * Prueba simple para iniciar sesión.
         * Corresponde a `ApiService.iniciarSesion(action, correo, password)`
         */
        fun probarIniciarSesionSimple(correo: String, password: String) {
            Log.d("PRUEBA_LOGIN", "Iniciando prueba: Login para $correo")

            // Usamos "login" como el action que requiere tu API
            ApiService.iniciarSesion("login", correo, password).enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        // Verificamos si hay respuesta y logueamos el estado
                        val estado = respuesta?.firstOrNull()?.Estado ?: "NO_RESPUESTA"
                        Log.d("PRUEBA_LOGIN", "RESPUESTA_LOGIN (Estado): $estado, Salida: ${respuesta?.firstOrNull()?.Salida}")
                    } else {
                        Log.e("PRUEBA_LOGIN", "ERROR_HTTP => ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    Log.e("PRUEBA_LOGIN", "ERROR_CONEXION => ${t.message}")
                }
            })
        }

        /**
         * Prueba simple para registrar un nuevo usuario.
         * Corresponde a `ApiService.registrarUsuario(action, nombre, apellido, correo, password)`
         */
        fun probarRegistrarUsuarioSimple(nombre: String, apellido: String, correo: String, password: String) {
            Log.d("PRUEBA_REGISTRO", "Iniciando prueba de registro para $correo...")

            // Usamos "registro" como el action que requiere tu API (comúnmente)
            ApiService.registrarUsuario(
                action = "registro", // o "register", según tu API en apiAcceso.php
                nombre = nombre,      // vchnombre
                apellido = apellido,  // vchapellido
                correo = correo,      // vchcorreo
                password = password   // vchpassword
            ).enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()

                        // Asumimos que la respuesta es una lista con 1 o 0 elementos
                        val estado = respuesta?.firstOrNull()?.Estado ?: "NO_RESPUESTA"
                        val salida = respuesta?.firstOrNull()?.Salida ?: "Error desconocido"

                        if (estado == "OK") {
                            Log.i("PRUEBA_REGISTRO", "✅ REGISTRO ÉXITO: $salida. ID de usuario potencial: ${respuesta?.firstOrNull()?.user_id}")
                        } else {
                            Log.w("PRUEBA_REGISTRO", "⚠️ REGISTRO FALLIDO: $salida")
                        }
                    } else {
                        // Manejo de códigos HTTP como 404, 500, etc.
                        Log.e("PRUEBA_REGISTRO", "❌ Error HTTP ${response.code()} en Registro.")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    Log.e("PRUEBA_REGISTRO", "🔥 Error de red en Registro: ${t.message}", t)
                }
            })
        }
    }