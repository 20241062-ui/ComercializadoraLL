package com.example.comercializadorall.Vista

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
// --- Importaciones de la lógica del Carrito ---
import com.example.comercializadorall.Modelo.CarritoModel
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CarritoPresenter
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import com.example.comercializadorall.Modelo.ICarritoModel // Asegúrate que esta importación sea correcta si usas ICarritoModel en el Presenter

class VistaDetalle : AppCompatActivity(), ICarritoView {

    // Vistas consolidadas
    private lateinit var txtNombre: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtStock: TextView
    private lateinit var imgProducto: ImageView
    private lateinit var txtmarca: TextView
    private lateinit var btnComprar: Button
    // Agregamos la vista que faltaba de la segunda clase
    private lateinit var txtCobertura: TextView
    // NOTA: Asegúrate de añadir esta vista al XML activity_vista_detalle si no está.

    // Propiedades de Carrito y Producto
    private lateinit var carritoPresenter: CarritoPresenter
    private var productoActual: clsProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vista_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicialización de Vistas (Asegúrate de incluir txtCobertura)
        txtNombre = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtStock = findViewById(R.id.txtStock)
        imgProducto = findViewById(R.id.txtImagen)
        txtmarca = findViewById(R.id.txtMarca)
        btnComprar = findViewById(R.id.buttonComprar)
        // Inicialización de la vista agregada
        // ATENCIÓN: Debes usar el ID correcto de tu XML si lo agregaste
        // Si no agregaste la vista, comenta esta línea o crea el TextView en el XML.
        // txtCobertura = findViewById(R.id.txtCobertura)


        // 2. Inicializar Presenter
        carritoPresenter = CarritoPresenter(this, CarritoModel(this))

        // 3. Cargar Datos del Producto desde el Intent
        cargarDatosProducto()

        // 4. Listener para Añadir al Carrito (con llamada al Presenter)
        btnComprar.setOnClickListener {
            productoActual?.let { producto ->
                carritoPresenter.agregarAlCarrito(producto)
            } ?: run {
                mostrarMensaje("Error: Información del producto no disponible para agregar.")
            }
        }

        // --- Navegación Inferior ---
        val openLoginImage: ImageView = findViewById(R.id.imgPerfil)
        val imgInfo: ImageView = findViewById(R.id.imgInfo)
        val imgInicio: ImageView = findViewById(R.id.imgInicio)
        val imgCategorias: ImageView = findViewById(R.id.imgCategorias)
        val imgEmpresa: ImageView = findViewById(R.id.imgEmpresa)

        openLoginImage.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        imgInfo.setOnClickListener {
            startActivity(Intent(this, InformaciondelaEmpresa::class.java))
        }
        imgInicio.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        imgCategorias.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }
    }

    private fun cargarDatosProducto() {
        val intent = intent

        // 1. Extraer los datos del Intent (incluyendo Cobertura, Serie y la forma de tu segunda clase)
        val noSerie = intent.getStringExtra("producto_id") ?: intent.getStringExtra("producto_serie") // Soporte para ambos nombres
        val nombre = intent.getStringExtra("producto_nombre")
        val descripcion = intent.getStringExtra("producto_descripcion")
        val imagenUrl = intent.getStringExtra("producto_imagen")
        val marca = intent.getStringExtra("producto_marca")
        val cobertura = intent.getStringExtra("producto_cobertura") // Dato de la segunda clase
        val categoria = intent.getStringExtra("producto_categoria")
        val precio = intent.getFloatExtra("producto_precio", 0.0f)
        val stock = intent.getIntExtra("producto_stock", 0)

        // 2. Crear el objeto clsProductos
        if (noSerie != null) {
            // ATENCIÓN: Asumo que ya ajustaste clsProductos para aceptar todos estos parámetros
            // (vchCobertura es el nuevo parámetro)
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

        // 3. Asignar los datos a las Vistas
        productoActual?.let { p ->
            txtNombre.text = p.vchNombre
            txtDescripcion.text = p.vchDescripcion
            txtStock.text = "Stock: ${p.intStock} Unidades"
            txtPrecio.text = String.format("$ %.2f", p.floPrecioUnitario)
            txtmarca.text = p.vchMarca
            // Si txtCobertura está inicializado:
            // if (::txtCobertura.isInitialized) txtCobertura.text = "Cobertura: ${p.vchCobertura}"

            // Carga de imagen con Glide
            if (p.vchImagen.isNotEmpty()) {
                val URL_BASE_IMAGENES = "http://comercializadorall.grupoctic.com/ComercializadoraLL/img/"
                val urlCompleta = URL_BASE_IMAGENES + p.vchImagen
                Glide.with(this).load(urlCompleta).into(imgProducto)
            } else {
                imgProducto.setImageResource(R.drawable.blacklogo)
            }
        }
    }

    // --- Implementación de ICarritoView ---

    override fun mostrarCarrito(lista: MutableList<clsProductos>) {
        // No se usa aquí.
    }

    override fun mostrarMensaje(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navegarALogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}