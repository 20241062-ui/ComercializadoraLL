package com.example.comercializadorall.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.Presentador.LoginPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Contracts.LoginContract

class Login : AppCompatActivity(), LoginContract {
    private lateinit var etCorreo: EditText // Renombrado de etEmail
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button
    private lateinit var tvRegistrar: TextView
    private lateinit var presentador: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etCorreo = findViewById(R.id.edtUsuario) // Mantenemos IDs, pero la variable se llama correo
        etPassword = findViewById(R.id.edtpassword)
        btnAcceder = findViewById(R.id.btnAcceder)
        tvRegistrar = findViewById(R.id.txtRegistrar)


        val model = LoginModel()

        val presentador = LoginPresenter(this, model)

        btnAcceder.setOnClickListener {
            val correo = etCorreo.text.toString() // Variable renombrada
            val password = etPassword.text.toString()
            presentador.iniciarSesion(correo, password) // Método presentador actualizado (usa correo)
        }

        tvRegistrar.setOnClickListener {
            startActivity(Intent(this, Registro::class.java)) // Actividad renombrada
        }
    }

    override fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarAMain() {
        startActivity(Intent(this, MainActivity::class.java)) // Actividad renombrada
        finish()
    }
}