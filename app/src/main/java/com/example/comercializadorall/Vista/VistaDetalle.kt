package com.example.comercializadorall.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.comercializadorall.R

class VistaDetalle : AppCompatActivity() {
    private lateinit var txtNombre: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtStock: TextView
    private lateinit var imgProducto: ImageView
    private lateinit var txtmarca: TextView
    private lateinit var txtCobertura: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vista_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtNombre=findViewById(R.id.txtNombre)
        txtPrecio=findViewById(R.id.txtPrecio)
        txtDescripcion=findViewById(R.id.txtDescripcion)
        txtStock=findViewById(R.id.txtStock)
        imgProducto=findViewById(R.id.txtImagen)
        txtmarca=findViewById(R.id.txtmarca)

        cargarDatosProducto()
        val openLoginImage: ImageView = findViewById(R.id.imgPerfil)
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

    private fun cargarDatosProducto() {
        // 1. Obtener el Intent que inició esta Activity
        val intent = intent

        // 2. Extraer los datos usando las mismas claves definidas en MainActivity
        val nombre = intent.getStringExtra("producto_nombre")
        val descripcion = intent.getStringExtra("producto_descripcion")
        val imagenUrl = intent.getStringExtra("producto_imagen")
        val marca = intent.getStringExtra("producto_marca")
        val precio = intent.getFloatExtra("producto_precio", 0.0f)
        val stock = intent.getIntExtra("producto_stock", 0)
        // 3. 🚨 Asignar los datos a las Vistas

        // TextViews
        txtNombre.text = nombre
        txtDescripcion.text = descripcion
        txtStock.text = "Stock: $stock Unidades"

        // Formato de precio
        txtPrecio.text = "Precio: $ ${String.format("%.2f", precio)}"

        // ImageView (Carga con Glide)
        if (imagenUrl != null) {
            val URL_BASE_IMAGENES = "http://comercializadorall.grupoctic.com/ComercializadoraLL/img/"
            val urlCompleta = URL_BASE_IMAGENES + imagenUrl

            Glide.with(this)
                .load(urlCompleta)
                .into(imgProducto)
            }
        txtmarca.text = "Marca: $marca"
        }
    }