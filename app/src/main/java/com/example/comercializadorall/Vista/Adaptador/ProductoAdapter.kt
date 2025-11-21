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
import com.example.comercializadorall.Modelo.clsProductos // Modelo actualizado
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.VistaDetalle // Vista de detalle renombrada

// 1. Definir interfaz para manejar el clic y comunicar el video
interface OnVideoSelectedListener {
    fun onVideoSelected(videoFileName: String)
}
class ProductoAdaptador(val contexto: Context, val listaproductos:List<clsProductos>): RecyclerView.Adapter<ProductoAdaptador.ProductoViewHolder>() { // Clases renombradas

    class ProductoViewHolder(control: View): RecyclerView.ViewHolder(control){ // Clase renombrada
        // IDs asumidos: imgFoto, txtNombre, txtDescripcion
        var imgproducto: ImageView =control.findViewById(R.id.imgProducto)
        val txtnombre: TextView =control.findViewById(R.id.txtNombre)
        val txtprecio: TextView =control.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder { // Clase renombrada
        // Layout asumido: producto_layout
        val view= LayoutInflater.from(parent.context).inflate(R.layout.activity_producto_layout,parent,false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder,position: Int) { // Clase renombrada

        val producto=listaproductos[position]

        holder.txtnombre.text = producto.vchNombre
        holder.txtprecio.text = "Precio: $${producto.floPrecioUnitario}"

        Glide.with(contexto)
            .load("https://comercializadorall.grupoctic.com/ComercializadoraLL/Recursos/" + producto.vchImagen)
            .into(holder.imgproducto)
        holder.imgproducto.setOnClickListener {
            verDetalleProducto(producto)
        }
    }
    fun verDetalleProducto(producto: clsProductos){
        val intent= Intent(contexto, VistaDetalle::class.java).apply{
            // Usamos las claves de producto correctas
            putExtra("producto_id",producto.vchNo_Serie)
            putExtra("producto_nombre",producto.vchNombre)
            putExtra("producto_descripcion",producto.vchDescripcion)
            putExtra("producto_sinopsis", "Precio: ${producto.floPrecioUnitario} | Stock: ${producto.intStock}")
            putExtra("producto_imagen",producto.vchImagen)
            // Usamos el nombre de la imagen para el video (asumido)
            putExtra("producto_video",producto.vchImagen)
        }
        contexto.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return listaproductos.size
    }
}
