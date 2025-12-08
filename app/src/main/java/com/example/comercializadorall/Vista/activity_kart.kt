package com.example.comercializadorall.Vista

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comercializadorall.Adaptadores.CarritoAdapter
import com.example.comercializadorall.Modelo.CarritoModel
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CarritoPresenter
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import com.example.comercializadorall.databinding.ActivityCarritocompraBinding

class activity_kart : AppCompatActivity(), ICarritoView {
    private lateinit var binding: ActivityCarritocompraBinding
    private lateinit var presenter: CarritoPresenter
    private lateinit var adapter: CarritoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityCarritocompraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CarritoPresenter(this, CarritoModel(this))

        adapter = CarritoAdapter(mutableListOf())
        binding.rvProductosCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvProductosCarrito.adapter = adapter

        presenter.cargarCarrito()

    }
    override fun mostrarCarrito(lista: MutableList<clsProductos>) {
        adapter.actualizarLista(lista)
    }

    override fun mostrarMensaje(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun navegarALogin() {
        TODO("Not yet implemented")
    }
}