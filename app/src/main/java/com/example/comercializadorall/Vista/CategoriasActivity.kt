package com.example.comercializadorall.Vista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.leanback.widget.Presenter
import androidx.recyclerview.widget.RecyclerView
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CategoriasPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Adaptador.ProductoAdaptador
import com.example.comercializadorall.Vista.Contracts.CategoriasContract
import com.google.zxing.integration.android.IntentIntegrator

class CategoriasActivity : AppCompatActivity(), CategoriasContract.View {
    private lateinit var editTextSearch: EditText
    private lateinit var btnScanQR: ImageButton
    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: CategoriasContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categorias)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicializarVistas()
        // 2. Inicialización del Presenter
        // Aquí pasas la Activity (this) que implementa CategoriasContract.View
        presenter = CategoriasPresenter(this)

        // 3. Configuración de Listeners
        configurarListeners()

        // 4. Iniciar la carga de categorías iniciales
        presenter.iniciar()
    }
    override fun mostrarCargando(isLoading: Boolean) {
        if (isLoading) {
            // Mostrar el indicador de carga y ocultar el contenido principal si es necesario
            progressBar.visibility = View.VISIBLE
        } else {
            // Ocultar el indicador de carga
            progressBar.visibility = View.GONE
        }
    }
    private val qrScannerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            val codigoQR = scanResult.contents

            if (codigoQR != null) {
                // Si el escaneo es exitoso, pasa el código al Presenter
                presenter.escanearQR(codigoQR)
            } else {
                mostrarMensajeError("Escaneo cancelado o código no reconocido.")
            }
        }
    }

    override fun onDestroy() {
        presenter.detener()
        super.onDestroy()
    }
    private fun inicializarVistas() {
        // Asegúrate de que estos IDs coincidan con tu activity_categorias.xml
        editTextSearch = findViewById(R.id.editTextSearch)
        btnScanQR = findViewById(R.id.btnScanQR)
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias)
        progressBar = findViewById(R.id.progressBar)
    }
    private fun configurarListeners() {
        // A. Búsqueda al presionar ENTER/Buscar en el teclado
        editTextSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 1. Obtener y limpiar la query (eliminar espacios iniciales/finales)
                val query = textView.text.toString().trim()

                // 2. VALIDACIÓN CRÍTICA: Asegurarse de que el texto no esté vacío.
                if (query.isEmpty()) {
                    // Si está vacío, muestra un mensaje y NO llama a la API (evita el 400)
                    Toast.makeText(this, "Por favor, introduce un término de búsqueda.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }

                // 3. Si la query es válida, llama al Presenter
                presenter.buscarProductos(query)

                // Ocultar teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        // B. Escaneo QR al presionar el botón
        btnScanQR.setOnClickListener {
            iniciarEscaneoQR()
        }
    }
    private fun iniciarEscaneoQR() {
        // Configuración de la librería ZXing (o la que uses)
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR del producto")
        integrator.setCameraId(0) // Usar cámara trasera
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)

        // Lanzar la actividad usando el Launcher registrado
        qrScannerLauncher.launch(integrator.createScanIntent())
    }

    override fun mostrarCategorias(categorias: List<clsProductos>) {
        // 🛑 1. CRÍTICO: Asegurarse de que el LayoutManager esté configurado para el GRID
        // Esto es necesario para mostrar las categorías en 2 columnas (spanCount=2)
        if (recyclerViewCategorias.layoutManager == null) {
            recyclerViewCategorias.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(this, 2)
        }

        // 🛑 2. CREAR EL ADAPTADOR
        // Usaremos el ProductoAdaptador para reutilizar el "cubo" de producto.
        val adaptador = ProductoAdaptador(
            contexto = this,
            listaproductos = categorias // La lista de productos (que actúa como categorías iniciales)
        ) { productoSeleccionado ->
            // Lógica de click: Navegar al detalle (la misma que usas en el adaptador principal)
            navegarADetalleProducto(productoSeleccionado)
        }

        // 🛑 3. ASIGNAR EL ADAPTADOR
        recyclerViewCategorias.adapter = adaptador
    }

    override fun mostrarResultadosBusqueda(productos: List<clsProductos>) {
        // 💡 Cambio 1: Cambiar a LinearLayoutManager para una lista vertical limpia
        recyclerViewCategorias.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)

        // 💡 Cambio 2: Usar el Adaptador de Producto
        val adaptador = ProductoAdaptador(
            contexto = this,
            listaproductos = productos
        ) { productoSeleccionado ->
            navegarADetalleProducto(productoSeleccionado)
        }

        recyclerViewCategorias.adapter = adaptador
    }

    override fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, "ERROR: $mensaje", Toast.LENGTH_LONG).show()
    }

    override fun navegarADetalleProducto(producto: clsProductos) {
        val intent = Intent(this, VistaDetalle::class.java).apply { // Reemplaza VistaDetalle con tu Activity
            // Pasar todos los extras del producto (como haces en la vista principal)
            putExtra("producto_id", producto.vchNo_Serie)
            putExtra("producto_nombre", producto.vchNombre)
            putExtra("producto_descripcion", producto.vchDescripcion)
            putExtra("producto_precio", producto.floPrecioUnitario)
            putExtra("producto_stock", producto.intStock)
            putExtra("producto_imagen", producto.vchImagen)
            putExtra("producto_marca", producto.vchMarca)
            putExtra("producto_categoria", producto.vchCategoria)
            putExtra("producto_cobertura", producto.vchCobertura)
        }
        startActivity(intent)
    }
}