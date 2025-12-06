package com.example.comercializadorall.Modelo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarritoModel(private val context: Context) : ICarritoModel {

    private val prefs = context.getSharedPreferences("CARRITO_PREF", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "carrito_data"
    private val sessionPrefs = context.getSharedPreferences("SESION_PREF", Context.MODE_PRIVATE)
    private val sessionKey = "usuario_logueado"

    override fun agregarProducto(producto: clsProductos) {
        val carrito = obtenerCarrito()

        val existente = carrito.find { it.vchNo_Serie == producto.vchNo_Serie }

        if (existente != null) {
            existente.intCantidadCarrito++
        } else {
            carrito.add(producto)
        }
        guardarCarrito(carrito)
    }

    override fun obtenerCarrito(): MutableList<clsProductos> {
        val json = prefs.getString(key, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<clsProductos>>() {}.type

        return gson.fromJson(json, type)
    }

    override fun guardarCarrito(lista: MutableList<clsProductos>) {
        val json = gson.toJson(lista)
        prefs.edit().putString(key, json).apply()
    }

    override fun limpiarCarrito() {
        prefs.edit().remove(key).apply()
    }

    override fun estaSesionIniciada(): Boolean {
        val userId = sessionPrefs.getString(sessionKey, null)
        return !userId.isNullOrEmpty()
    }
}
