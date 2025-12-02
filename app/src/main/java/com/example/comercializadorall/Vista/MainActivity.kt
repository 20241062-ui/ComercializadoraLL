package com.example.comercializadorall.Vista

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.MainPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Contracts.MainContract
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.comercializadorall.Modelo.ReproducirModel
import com.example.comercializadorall.Vista.Adaptador.ProductoAdaptador

class MainActivity : AppCompatActivity(), MainContract {

    // Componentes de la vista
    private lateinit var rcvProductos: RecyclerView
    private lateinit var presenter: MainPresenter

    // Componentes del reproductor
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var imgPerfil:ImageView
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 101

    // Modelo para la URL de video
    private val reproducirModel = ReproducirModel()
    private fun requestNotificationPermission() {
        // Verificar si estamos en Android 13 (API 33) o superior
        if (Build.VERSION.SDK_INT >= 33) {
            // Verificar si el permiso ya fue concedido
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicitar el permiso al usuario
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestNotificationPermission()
        rcvProductos = findViewById(R.id.rcvProductos)
        playerView = findViewById(R.id.playerView)


        // --- 2. Configuración de RecyclerView ---
        rcvProductos.layoutManager = LinearLayoutManager(this)

        // --- 3. Inicialización del Presenter ---
        presenter = MainPresenter(this)
        presenter.obtenerProductos()

        iniciarReproductorVideoFijo()
        val openLoginImage: ImageView = findViewById(R.id.imgPrefil)
        openLoginImage.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun iniciarReproductorVideoFijo() {
        val nombreVideoFijo = "video_promocional.mp4"
        val videoUrl = reproducirModel.obtenerUrlVideo(nombreVideoFijo)

        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    // 💡 MÉTODO REFACTORIZADO: Aquí se inicializa el Adaptador con la lógica de click.
    override fun mostrarProductos(productos: List<clsProductos>) {

        // 1. Crear el Adaptador, proporcionando la lógica de click como el tercer argumento (lambda).
        val adaptador = ProductoAdaptador(
            contexto = this,
            listaproductos = productos
        ) { productoSeleccionado ->
            // 💡 LÓGICA DE NAVEGACIÓN (Se ejecuta al hacer click en la imagen del RecyclerView)

            val intent = Intent(this, VistaDetalle::class.java).apply {
                // Pasar los datos del producto seleccionado
                putExtra("producto_id", productoSeleccionado.vchNo_Serie)
                putExtra("producto_nombre", productoSeleccionado.vchNombre)
                putExtra("producto_descripcion", productoSeleccionado.vchDescripcion)
                putExtra("producto_precio", productoSeleccionado.floPrecioUnitario)
                putExtra("producto_stock", productoSeleccionado.intStock)
                putExtra("producto_imagen", productoSeleccionado.vchImagen)
                putExtra("producto_marca", productoSeleccionado.vchMarca)
                putExtra("producto_categoria", productoSeleccionado.vchCategoria)
                putExtra("producto_cobertura",productoSeleccionado.vchCobertura)
            }
            startActivity(intent)
        }

        // 2. Asignar el adaptador al RecyclerView.
        rcvProductos.adapter = adaptador
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    // --- Métodos del ciclo de vida del reproductor (correctos) ---
    override fun onResume() {
        super.onResume()
        if (::exoPlayer.isInitialized) {
            exoPlayer.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (::exoPlayer.isInitialized) {
            exoPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}