package com.example.comercializadorall.Modelo

import com.example.comercializadorall.Vista.clsDatosRespuesta
import com.example.comercializadorall.Modelo.ifaceApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Patterns // Import necesario para la validación de email

/**
 * Modelo encargado de la lógica de negocio para el registro de usuarios,
 * incluyendo la validación de datos en el lado del cliente (UX)
 * antes de enviar la solicitud al servidor (API).
 */
class RegistroModel(private val apiService: ifaceApiService) {

    interface OnRegistroListener {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

    /**
     * Valida los datos localmente y luego llama a la API para registrar el usuario.
     */
    fun registrarUsuario(nombre: String, apellido: String, correo: String, password: String, rolFijo: String, listener: OnRegistroListener) {

        // --- 1. VALIDACIÓN DEL LADO DEL CLIENTE (Mejor Experiencia de Usuario) ---

        // 1.1. Verificación de campos obligatorios
        if (nombre.isBlank() || apellido.isBlank() || correo.isBlank() || password.isBlank()) {
            listener.onFailure("Todos los campos (nombre, apellido, correo y contraseña) son obligatorios.")
            return
        }

        // 1.2. Validación de Nombre y Apellido: No deben contener números.
        // Se utiliza la expresión regular simple para buscar cualquier dígito.
        if (nombre.contains(Regex("[0-9]")) || apellido.contains(Regex("[0-9]"))) {
            listener.onFailure("El nombre y el apellido no deben contener números.")
            return
        }

        // 1.3. Validación de formato de email utilizando la utilidad estándar de Android.
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            listener.onFailure("El formato del correo electrónico es inválido. Por favor, revísalo.")
            return
        }

        // 1.4. Validación de longitud mínima de contraseña (debe coincidir con la API, que espera 8)
        if (password.length < 8) {
            listener.onFailure("La contraseña debe tener al menos 8 caracteres para ser segura.")
            return
        }

        // --- 2. LLAMADA A LA API (Si la validación local es exitosa) ---
        apiService.registrarUsuario("registrar", nombre, apellido, correo, password)
            .enqueue(object : Callback<List<clsDatosRespuesta>> {
                override fun onResponse(
                    call: Call<List<clsDatosRespuesta>>,
                    response: Response<List<clsDatosRespuesta>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            // El API de registro devuelve 'true' como String
                            if (datos.isNotEmpty() && datos[0].Estado.equals("true", ignoreCase = true)) {
                                listener.onSuccess(datos[0].Salida)
                            } else {
                                // Esto captura errores como 'El correo ya está registrado' (desde la API)
                                listener.onFailure(datos[0].Salida)
                            }
                        } ?: listener.onFailure("Respuesta vacía o en formato incorrecto del servidor.")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        listener.onFailure("Error ${response.code()} en la respuesta del servidor: $errorBody")
                    }
                }

                override fun onFailure(call: Call<List<clsDatosRespuesta>>, t: Throwable) {
                    listener.onFailure("Error de conexión: Asegúrate de que tienes conexión a internet. Mensaje: ${t.message}")
                }
            })
    }
}