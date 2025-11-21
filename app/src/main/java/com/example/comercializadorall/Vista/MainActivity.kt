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

class MainActivity : AppCompatActivity(), MainContract {

    private lateinit var rcvProductos: RecyclerView
    private lateinit var presenter: MainPresenter
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
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
        rcvProductos=findViewById(R.id.rcvProductos)
        rcvProductos.layoutManager= LinearLayoutManager(this)
        playerView = findViewById(R.id.playerView) // ⚠️ ASUMIMOS ESTE ID EN activity_main.xml
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer
        presenter=MainPresenter(this)
        presenter.obtenerProductos()
        playerView = findViewById(R.id.playerView) // ⚠️ Asegúrate de que este ID exista en activity_main.xml
        iniciarReproductorVideoFijo()
    }
    private fun iniciarReproductorVideoFijo() {
        // ⚠️ DEFINE QUÉ VIDEO QUIERES REPRODUCIR POR DEFECTO
        // Usaremos un nombre de archivo de ejemplo:
        val nombreVideoFijo = "video_promocional.mp4"

        // Obtener la URL usando el modelo
        val videoUrl = reproducirModel.obtenerUrlVideo(nombreVideoFijo)

        // Inicializar y preparar el ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        // Puedes agregar aquí la opción de pausar/reanudar con un botón si lo deseas.
    }

    override fun mostrarProductos(productos: List<clsProductos>) {
        // NOTA: El constructor del adaptador ahora es simple, sin el listener
        val adaptador = ProductoAdapter(this, productos)
        rcvProductos.adapter = adaptador
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onVideoSelected(videoFileName: String) {
        presenter.reproducirProducto(videoFileName)
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
