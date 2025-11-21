package com.example.comercializadorall.Vista
// Mantenemos esta data class ya que los campos 'Estado' y 'Salida' coinciden con tu API
data class clsDatosRespuesta(val Estado: String, val Salida: String, val user_id: Int? = null)