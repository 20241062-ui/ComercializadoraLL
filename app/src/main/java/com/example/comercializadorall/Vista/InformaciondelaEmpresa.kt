package com.example.comercializadorall.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comercializadorall.R

class InformaciondelaEmpresa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_informaciondela_empresa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val openLoginImage: ImageView = findViewById(R.id.imgInicio)
        val imgInfo: ImageView = findViewById(R.id.imgInfo)
        val imgInicio: ImageView = findViewById(R.id.imgInicio)
        val imgCategorias: ImageView = findViewById(R.id.imgCategorias)
        val imgEmpresa: ImageView = findViewById(R.id.imgEmpresa)
        openLoginImage.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        imgEmpresa.setOnClickListener {
            val intent = Intent(this, InformaciondelaEmpresa::class.java)
            startActivity(intent)
        }
        imgInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        imgInfo.setOnClickListener {
            // **NOTA:** Reemplaza 'ActivityInfo' con el nombre de tu Activity real
            // cuando la hayas creado.
            // val intent = Intent(this, ActivityInfo::class.java)
            // startActivity(intent)
        }

        // Evento para imgCategorias (Activity pendiente)
        imgCategorias.setOnClickListener {
            // **NOTA:** Reemplaza 'ActivityCategorias' con el nombre de tu Activity real
            // cuando la hayas creado.
            // val intent = Intent(this, ActivityCategorias::class.java)
            // startActivity(intent)
        }
    }
}