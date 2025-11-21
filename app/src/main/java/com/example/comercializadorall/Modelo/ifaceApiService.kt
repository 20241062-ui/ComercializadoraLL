package com.example.comercializadorall.Modelo

import android.telecom.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ifaceApiService {
    @GET("apiProductos.php")
    fun obtenerPeliculas(): Call<List<clsProductos>>
}