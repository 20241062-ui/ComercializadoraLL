package com.example.comercializadorall.Vista.Adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.ProductoVista
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.VistaDetalle

class ProductoAdaptador(val contexto: Context, val listaproductos:List<ProductoVista>): RecyclerView.Adapter<ProductoAdaptador.ProductoViewHolder>() { // Clases renombradas

    class ProductoViewHolder(control: View): RecyclerView.ViewHolder(control){
        val txtnombre: TextView =control.findViewById(R.id.txtNombre)
        var imgproducto: ImageView =control.findViewById(R.id.imgProducto)
        val txtprecio: TextView =control.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder { // Clase renombrada
        // Layout asumido: producto_layout
        val view= LayoutInflater.from(parent.context).inflate(R.layout.producto_layout,parent,false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder,position: Int) { // Clase renombrada

        val producto=listaproductos[position]

        holder.txtnombre.text = producto.nombreDisplay // Nombre formateado para mostrar
        holder.txtprecio.text = producto.precioFormatted // Precio ya formateado

        // La URL ya está completa y lista
        Glide.with(contexto).load(producto.urlImagenCompleta).fitCenter().into(holder.imgproducto)
    }

    fun verDetalleProducto(producto: clsProductos){
        val intent= Intent(contexto, VistaDetalle::class.java).apply{
            // Usamos las claves de producto correctas
            putExtra("producto_id",producto.vchNo_Serie)
            putExtra("producto_nombre",producto.vchNombre)
            putExtra("producto_descripcion",producto.vchDescripcion)
            putExtra("producto_precio", producto.floPrecioUnitario)
            putExtra("producto_stock", producto.intStock)
            putExtra("producto_imagen",producto.vchImagen)
        }
        contexto.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return listaproductos.size
    }
}
