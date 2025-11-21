package com.example.comercializadorall.Vista

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.MainPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Contracts.MainContract
import androidx.media3.common.MediaItem // Necesario para ExoPlayer
import androidx.media3.exoplayer.ExoPlayer // Necesario para ExoPlayer
import androidx.media3.ui.PlayerView // Necesario para la vista del reproductor
import com.example.comercializadorall.Modelo.ReproducirModel
import com.example.comercializadorall.Vista.Adaptador.ProductoAdaptador


class MainActivity : AppCompatActivity(), MainContract {

    // Componentes de la vista
    private lateinit var rcvProductos: RecyclerView
    private lateinit var presenter: MainPresenter

    // Componentes del reproductor
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer

    // Modelo para la URL de video
    private val reproducirModel = ReproducirModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- 1. Inicialización de Vistas ---
        rcvProductos = findViewById(R.id.rcvProductos)
        playerView = findViewById(R.id.playerView) // Inicialización de PlayerView

        // --- 2. Configuración de RecyclerView ---
        rcvProductos.layoutManager = LinearLayoutManager(this)

        // --- 3. Inicialización del Presenter ---
        presenter = MainPresenter(this)
        presenter.obtenerProductos()

        iniciarReproductorVideoFijo()
    }

    private fun iniciarReproductorVideoFijo() {
        val nombreVideoFijo = "video_promocional.mp4"
        val videoUrl = reproducirModel.obtenerUrlVideo(nombreVideoFijo)

        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer // Vinculamos la vista con el motor

        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun mostrarProductos(productos: List<clsProductos>) {
        val adaptador = ProductoAdaptador(this, productos)
        rcvProductos.adapter = adaptador
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

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
