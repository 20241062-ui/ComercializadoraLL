package com.example.comercializadorall.vista

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comercializadorall.Modelo.*
import com.example.comercializadorall.Presentador.*
import com.example.comercializadorall.Adaptadores.CarritoAdapter
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import com.example.comercializadorall.databinding.ActivityCarritocompraBinding

class CarritoActivity : AppCompatActivity(), ICarritoView {

    private lateinit var binding: ActivityCarritocompraBinding
    private lateinit var presenter: CarritoPresenter
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

