package com.example.comercializadorall.Modelo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarritoModel(private val context: Context) : ICarritoModel {


    private val PREF_NAME_CARRITO = "CARRITO_PREF"
    private val PREF_KEY_CARRITO = "carrito_data"
    private val PREF_NAME_SESSION = "SESION_PREF"
    private val PREF_KEY_SESSION = "usuario_logueado"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_CARRITO, Context.MODE_PRIVATE)
    private val sessionPrefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_SESSION, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val listType = object : TypeToken<MutableList<clsProductos>>() {}.type


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
        val json = prefs.getString(PREF_KEY_CARRITO, null) ?: return mutableListOf()
        return gson.fromJson(json, listType)
    }

    override fun guardarCarrito(lista: MutableList<clsProductos>) {
        val json = gson.toJson(lista)
        prefs.edit().putString(PREF_KEY_CARRITO, json).apply()
    }

    override fun limpiarCarrito() {
        prefs.edit().remove(PREF_KEY_CARRITO).apply()
    }

    override fun eliminarProducto(producto: clsProductos) {
        val carritoJson = prefs.getString(PREF_KEY_CARRITO, null) ?: return
        val listaActual: MutableList<clsProductos> = gson.fromJson(carritoJson, listType)

        listaActual.removeAll { it.vchNo_Serie == producto.vchNo_Serie }

        prefs.edit()
            .putString(PREF_KEY_CARRITO, gson.toJson(listaActual))
            .apply()
    }

    override fun estaSesionIniciada(): Boolean {
        val userId = sessionPrefs.getString(PREF_KEY_SESSION, null)
        return !userId.isNullOrEmpty()
    }
}