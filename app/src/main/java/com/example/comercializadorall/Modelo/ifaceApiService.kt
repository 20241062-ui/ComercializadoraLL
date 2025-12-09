package com.example.comercializadorall.Modelo

import retrofit2.Call
import com.example.comercializadorall.Vista.clsDatosRespuesta
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ifaceApiService {
    @GET("apiProductos.php")
    fun obtenerProductos(): Call<List<clsProductos>>


    @GET("apiProductoPorCodigo.php")
    fun obtenerProductoPorCodigo(@Query("codigo") codigo: String): Call<clsProductos>
    @GET("apiBuscarProductos.php")
    fun buscarProductosPorQuery(@Query("q") query: String): Call<List<clsProductos>>
    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun registrarUsuario(
        @Field("action") action: String,
        @Field("vchnombre") nombre: String, // Campo actualizado a vchnombre
        @Field("vchapellido") apellido: String, // Campo nuevo (según tu API)
        @Field("vchcorreo") correo: String, // Campo actualizado a vchcorreo
        @Field("vchpassword") password: String // Campo actualizado a vchpassword
    ): Call<List<clsDatosRespuesta>>

    // 3. Login
    @FormUrlEncoded
    @POST("apiAcceso.php")
    fun iniciarSesion(
        @Field("action") action: String,
        @Field("vchcorreo") correo: String, // Campo actualizado a vchcorreo
        @Field("vchpassword") password: String // Campo actualizado a vchpassword
    ): Call<List<clsDatosRespuesta>>
}