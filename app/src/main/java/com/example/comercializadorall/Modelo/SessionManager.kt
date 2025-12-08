package com.example.comercializadorall.Modelo
import android.content.SharedPreferences

class SessionManager(
    private val sessionPrefs: SharedPreferences,
    private val sessionKey: String
) {

    // El companion object con la clave estática SE ELIMINA.

    fun guardarSesionActiva(idUsuario: String) {
        sessionPrefs.edit().apply {
            // USAMOS la clave inyectada
            putString(sessionKey, idUsuario)
            apply()
        }
    }

    fun obtenerIdUsuarioActivo(): String? {
        return sessionPrefs.getString(sessionKey, null)
    }

    fun cerrarSesion() {
        sessionPrefs.edit().apply {
            remove(sessionKey)
            apply()
        }
    }

    fun estaSesionIniciada(): Boolean {
        return obtenerIdUsuarioActivo() != null
    }
}
