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

    // Instancias de objetos
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_CARRITO, Context.MODE_PRIVATE)
    private val sessionPrefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_SESSION, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Definición del tipo para Gson
    private val listType = object : TypeToken<MutableList<clsProductos>>() {}.type

    // --- Lógica del Carrito ---

    override fun agregarProducto(producto: clsProductos) {
        val carrito = obtenerCarrito()

        val existente = carrito.find { it.vchNo_Serie == producto.vchNo_Serie }

        if (existente != null) {
            // Si el producto ya existe, incrementa la cantidad en el carrito (asumo que intCantidadCarrito es el contador)
            existente.intCantidadCarrito++
        } else {
            // Si es nuevo, añade el producto a la lista
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
        // Usamos PREF_KEY_CARRITO para la clave de guardado
        prefs.edit().putString(PREF_KEY_CARRITO, json).apply()
    }

    override fun limpiarCarrito() {
        // Limpia toda la lista del carrito
        prefs.edit().remove(PREF_KEY_CARRITO).apply()
    }

    override fun eliminarProducto(producto: clsProductos) {
        val carritoJson = prefs.getString(PREF_KEY_CARRITO, null) ?: return
        val listaActual: MutableList<clsProductos> = gson.fromJson(carritoJson, listType)

        // Elimina todos los productos que coincidan con el No. Serie
        listaActual.removeAll { it.vchNo_Serie == producto.vchNo_Serie }

        // Guarda la lista modificada
        prefs.edit()
            .putString(PREF_KEY_CARRITO, gson.toJson(listaActual))
            .apply()
    }

    override fun estaSesionIniciada(): Boolean {
        // Usa las variables de sesión consistentes
        val userId = sessionPrefs.getString(PREF_KEY_SESSION, null)
        return !userId.isNullOrEmpty()
    }
}