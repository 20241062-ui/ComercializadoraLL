package com.example.comercializadorall.Modelo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarritoModel(
    private val context: Context,
    private val sessionManager: SessionManager // Inyectar SessionManager
): ICarritoModel {
    private val PREF_NAME_CARRITO = "CARRITO_PREF"
    private val PREF_KEY_CARRITO = "carrito_data"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME_CARRITO, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val listType = object : TypeToken<MutableList<clsProductos>>() {}.type


    override fun agregarProducto(producto: clsProductos) {
        if (!estaSesionIniciada()) return

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
        val claveCarrito = obtenerClaveCarritoPorUsuario()

        // Si no hay clave (no hay sesión), se devuelve una lista vacía.
        if (claveCarrito == null) return mutableListOf()

        val json = prefs.getString(claveCarrito, null) ?: return mutableListOf()
        return gson.fromJson(json, listType)
    }

    override fun guardarCarrito(lista: MutableList<clsProductos>) {
        val claveCarrito = obtenerClaveCarritoPorUsuario()

        if (claveCarrito == null) return

        val json = gson.toJson(lista)
        prefs.edit().putString(claveCarrito, json).apply()
    }

    override fun limpiarCarrito() {
        // 1. Obtener la clave única del usuario logueado
        val claveCarrito = obtenerClaveCarritoPorUsuario()

        // 2. Si no hay clave (no hay sesión), no hacemos nada
        if (claveCarrito == null) return

        // 3. Eliminar la clave específica del carrito del usuario
        prefs.edit().remove(claveCarrito).apply()
    }

    override fun eliminarProducto(producto: clsProductos) {
        val claveCarrito = obtenerClaveCarritoPorUsuario()

        if (claveCarrito == null) return

        val carritoJson = prefs.getString(claveCarrito, null) ?: return
        val listaActual: MutableList<clsProductos> = gson.fromJson(carritoJson, listType)

        listaActual.removeAll { it.vchNo_Serie == producto.vchNo_Serie }

        prefs.edit()
            .putString(claveCarrito, gson.toJson(listaActual))
            .apply()
    }

    override fun estaSesionIniciada(): Boolean {
        return sessionManager.estaSesionIniciada()
    }
    private fun obtenerClaveCarritoPorUsuario(): String? {
        val userId = sessionManager.obtenerIdUsuarioActivo()
        return if (userId.isNullOrEmpty()) null else "${PREF_KEY_CARRITO}_$userId"
    }
}