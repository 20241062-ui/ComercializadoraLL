package com.example.comercializadorall.Vista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.AppConstants.PREFS_NAME
import com.example.comercializadorall.Vista.AppConstants.SESSION_KEY
import com.example.comercializadorall.Vista.activity_kart

class Perfil : AppCompatActivity() {
    private val loginModel by lazy {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        LoginModel(prefs, SESSION_KEY)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnVerCarrito: Button = findViewById(R.id.btnVerCarrito)
        val btnCerrarSesion: Button = findViewById(R.id.btnCerrarSesion)

        // 2. Programar el evento de clic
        btnVerCarrito.setOnClickListener {
            // Crear un Intent para abrir la nueva Activity
            val intent = Intent(this, CarritoActivity::class.java)

            // Iniciar la nueva Activity
            startActivity(intent)
        }
        btnCerrarSesion.setOnClickListener {
            // 1. Usa la instancia 'loginModel' ya creada y llama a la función
            loginModel.cerrarSesion()

            // 2. Redirige a MainActivity y limpia la pila
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
        val imgInfo: ImageView = findViewById(R.id.imgInfo)
        val imgInicio: ImageView = findViewById(R.id.imgInicio)
        val imgCategorias: ImageView = findViewById(R.id.imgCategorias)

        imgInfo.setOnClickListener {
            val intent = Intent(this, InformaciondelaEmpresa::class.java)
            startActivity(intent)
        }
        imgInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Evento para imgCategorias (Activity pendiente)
        imgCategorias.setOnClickListener {
            val intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }
    }
}