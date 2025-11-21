package com.example.comercializadorall.Vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comercializadorall.Modelo.RegistroModel
import com.example.comercializadorall.Modelo.ifaceApiService // Interfaz actualizada
import com.example.comercializadorall.Presentador.RegistrPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Contracts.RegistroContract // Contrato actualizado
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Registro : AppCompatActivity(), RegistroContract {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var etNombre: EditText // Renombrado de etNombreUsuario
        lateinit var etApellido: EditText // NUEVO CAMPO AGREGADO
        lateinit var etCorreo: EditText // Renombrado de etEmail
        lateinit var etPasswordRegistro: EditText
        lateinit var btnRegistrar: Button
        lateinit var presentador: RegistrPresenter
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etNombre = findViewById(R.id.edtUNombre)
        etApellido = findViewById(R.id.edtApellido) // ⚠️ ASUMIMOS ESTE ID EN EL XML
        etCorreo = findViewById(R.id.edtCorreo)
        etPasswordRegistro = findViewById(R.id.edtPass)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        val gson = GsonBuilder().setLenient().create()
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        val retrofit = Retrofit.Builder()
            // URL Base actualizada a Productos/api/
            .baseUrl("https://comercializadorall.grupoctic.com/ComercializadoraLL/API/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(ifaceApiService::class.java) // Interfaz actualizada
        val model = RegistroModel(apiService)
        presentador = RegistrPresenter(this, model)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString() // NUEVO CAMPO
            val correo = etCorreo.text.toString()
            val password = etPasswordRegistro.text.toString()
            presentador.registrarUsuario(nombre, apellido, correo, password) // Llama al presentador con el nuevo campo
        }
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun registroExitoso() {
        finish()
    }
}