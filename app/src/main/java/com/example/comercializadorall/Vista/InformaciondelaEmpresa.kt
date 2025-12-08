package com.example.comercializadorall.Vista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.Modelo.SessionManager
import com.example.comercializadorall.Modelo.clsInformacion
import com.example.comercializadorall.R
import com.example.comercializadorall.Presentador.EmpresaPresenter
import com.example.comercializadorall.Vista.Contracts.EmpresaContract // Asegúrate de que la ruta del contrato es correcta

// ✅ La Activity debe implementar la interfaz View
class InformaciondelaEmpresa : AppCompatActivity(), EmpresaContract.View {

    private lateinit var presenter: EmpresaPresenter
    private lateinit var txtMisionContenido: TextView
    private lateinit var txtVisionContenido: TextView
    private lateinit var txtMisionTitulo: TextView
    private lateinit var txtVisionTitulo: TextView
    object AppConstants {
        const val PREFS_NAME = "TUS_PREFS"
        const val SESSION_KEY = "SESSION_ID"
    }

    private val sessionManager by lazy {
        val prefs = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)
        SessionManager(
            prefs,
            AppConstants.SESSION_KEY
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_informaciondela_empresa)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarVistas()

        presenter = EmpresaPresenter(this)
        presenter.cargarDatos()
    }

    // Función auxiliar para inicializar todas las vistas y eventos
    private fun inicializarVistas() {
        // Inicialización de Vistas (CRÍTICO: Deben existir en el XML)
        val openLoginImage: ImageView = findViewById(R.id.imgPerfil)
        val imgInicio: ImageView = findViewById(R.id.imgInicio)
        val imgCategorias: ImageView = findViewById(R.id.imgCategorias)

        txtMisionContenido = findViewById(R.id.txtMisionContenido)
        txtVisionContenido = findViewById(R.id.txtVisionContenido)
        txtMisionTitulo = findViewById(R.id.txtMisionTitulo) // Asumido ID
        txtVisionTitulo = findViewById(R.id.txtVisionTitulo) // Asumido ID


        openLoginImage.setOnClickListener {
            val idUsuario = sessionManager.obtenerIdUsuarioActivo()

            if (idUsuario != null) {
                val intent = Intent(this, Perfil::class.java)
                startActivity(intent)

            } else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }

        imgInicio.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        imgCategorias.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }
    }

    override fun mostrarInformacion(mision: clsInformacion, vision: clsInformacion) {
        // ✅ Ahora es seguro acceder a txtMisionContenido, etc.
        txtMisionContenido.text = mision.vchcontenido
        txtVisionContenido.text = vision.vchcontenido
        txtMisionTitulo.text = mision.vchtitulo
        txtVisionTitulo.text = vision.vchtitulo
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, "Error al cargar: $mensaje", Toast.LENGTH_LONG).show()
        Log.e("EmpresaView", "Error de carga: $mensaje")
    }
}