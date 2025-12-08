package com.example.comercializadorall.Vista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.comercializadorall.R
import com.example.comercializadorall.Modelo.CarritoModel
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CarritoPresenter
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import com.example.comercializadorall.Modelo.SessionManager
import com.example.comercializadorall.Vista.AppConstants

class VistaDetalle : AppCompatActivity(), ICarritoView {

    private lateinit var txtNombre: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtStock: TextView
    private lateinit var imgProducto: ImageView
    private lateinit var txtmarca: TextView
    private lateinit var btnComprar: Button
    private lateinit var txtCobertura: TextView

    private lateinit var carritoPresenter: CarritoPresenter
    private var productoActual: clsProductos? = null

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
        setContentView(R.layout.activity_vista_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombre = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtStock = findViewById(R.id.txtStock)
        imgProducto = findViewById(R.id.txtImagen)
        txtmarca = findViewById(R.id.txtMarca)
        btnComprar = findViewById(R.id.buttonComprar)

        carritoPresenter = CarritoPresenter(this, CarritoModel(this))

        cargarDatosProducto()

        btnComprar.setOnClickListener {
            if (sessionManager.estaSesionIniciada()) {
                productoActual?.let { producto ->
                    carritoPresenter.agregarAlCarrito(producto)
                    mostrarMensaje("Producto agregado. ID Usuario: ${sessionManager.obtenerIdUsuarioActivo()}")
                } ?: run {
                    mostrarMensaje("Error: Información del producto no disponible para agregar.")
                }
            } else {
                mostrarMensaje("¡Debes iniciar sesión para agregar productos al carrito!")
                navegarALogin()
            }
        }

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

    private fun cargarDatosProducto() {
        val intent = intent

        val noSerie = intent.getStringExtra("producto_id") ?: intent.getStringExtra("producto_serie")
        val nombre = intent.getStringExtra("producto_nombre")
        val descripcion = intent.getStringExtra("producto_descripcion")
        val imagenUrl = intent.getStringExtra("producto_imagen")
        val marca = intent.getStringExtra("producto_marca")
        val cobertura = intent.getStringExtra("producto_cobertura")
        val precio = intent.getFloatExtra("producto_precio", 0.0f)
        val stock = intent.getIntExtra("producto_stock", 0)

        if (noSerie != null) {
            productoActual = clsProductos(
                vchNo_Serie = noSerie,
                vchNombre = nombre ?: "",
                vchDescripcion = descripcion ?: "Sin descripción disponible.",
                vchImagen = imagenUrl ?: "",
                vchMarca = marca ?: "Marca Desconocida",
                floPrecioUnitario = precio,
                intStock = stock,
                vchCobertura = cobertura ?: "",
                intCantidadCarrito = 1
            )
        } else {
            mostrarMensaje("Error: El producto no tiene un ID único (No. Serie).")
            btnComprar.isEnabled = false
        }

        productoActual?.let { p ->
            txtNombre.text = p.vchNombre
            txtDescripcion.text = p.vchDescripcion
            txtStock.text = "Stock: ${p.intStock} Unidades"
            txtPrecio.text = String.format("$ %.2f", p.floPrecioUnitario)
            txtmarca.text = p.vchMarca

            if (p.vchImagen.isNotEmpty()) {
                val URL_BASE_IMAGENES = "http://comercializadorall.grupoctic.com/ComercializadoraLL/img/"
                val urlCompleta = URL_BASE_IMAGENES + p.vchImagen
                Glide.with(this).load(urlCompleta).into(imgProducto)
            } else {
                imgProducto.setImageResource(R.drawable.blacklogo)
            }
        }
    }

    override fun mostrarCarrito(lista: MutableList<clsProductos>) {}

    override fun mostrarMensaje(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navegarALogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}
