package com.example.comercializadorall.Vista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comercializadorall.Modelo.*
import com.example.comercializadorall.Presentador.*
import com.example.comercializadorall.Adaptadores.CarritoAdapter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.AppConstants.PREFS_NAME
import com.example.comercializadorall.Vista.AppConstants.SESSION_KEY
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import com.example.comercializadorall.databinding.ActivityCarritocompraBinding
import com.example.comercializadorall.Modelo.SessionManager


class CarritoActivity : AppCompatActivity(), ICarritoView,CarritoAdapter.OnItemClickListener {

    private lateinit var binding: ActivityCarritocompraBinding
    private lateinit var presenter: CarritoPresenter
    private lateinit var adapter: CarritoAdapter
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
        binding = ActivityCarritocompraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CarritoPresenter(this, CarritoModel(this))

        adapter = CarritoAdapter(mutableListOf())
        binding.rvProductosCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvProductosCarrito.adapter = adapter

        presenter.cargarCarrito()
        adapter.setOnItemClickListener(this)
        val openLoginImage: ImageView = findViewById(R.id.imgPerfil)
        val imgInfo: ImageView = findViewById(R.id.imgInfo)
        val imgInicio: ImageView = findViewById(R.id.imgInicio)
        val imgCategorias: ImageView = findViewById(R.id.imgCategorias)
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
        imgInfo.setOnClickListener {
            val intent = Intent(this, InformaciondelaEmpresa::class.java)
            startActivity(intent)
        }
        imgInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        imgCategorias.setOnClickListener {
            val intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onEliminarClick(producto: clsProductos, position: Int) {
        presenter.eliminarProducto(producto, position)
    }

    override fun mostrarCarrito(lista: MutableList<clsProductos>) {
        adapter.actualizarLista(lista)
    }

    override fun mostrarMensaje(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navegarALogin() {
         }
}

