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
import com.example.comercializadorall.Modelo.clsProductos // Importación de tu data class
import com.example.comercializadorall.R // Asegúrate de que este sea tu R real
import com.example.comercializadorall.Vista.producto_layout // Vista de detalle actualizada

class ProductoAdaptador(
    val contexto: Context,
    // La lista ahora es de clsProductos
    val listaproductos: List<clsProductos>
) : RecyclerView.Adapter<ProductoAdaptador.ProductoViewHolder>() {

    class ProductoViewHolder(control: View) : RecyclerView.ViewHolder(control) {
        // Los IDs de los controles se mantienen igual si el layout es idéntico
        val imgproducto: ImageView = control.findViewById(R.id.imgProducto)
        val txtnombre: TextView = control.findViewById(R.id.txtNombre)
        val txtdescripcion: TextView = control.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        // Vinculación con el layout de producto (se asume R.layout.producto_layout)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_producto_layout, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaproductos[position]

        // Asignación de datos usando los campos de clsProductos
        holder.txtnombre.text = producto.vchNombre
        holder.txtdescripcion.text = producto.vchDescripcion

        // *** IMPORTANTE: REVISAR LA CARGA DE IMAGEN ***
        // Si vchImagen es realmente un String con el nombre del archivo:
        val imageUrl = "https://javier.grupoctic.com/Productos/img/" + producto.vchImagen

        // Si vchImagen es un Int que es un Resource ID (R.drawable.X), usa:
        // val imageResourceId = producto.vchImagen

        // Carga con Glide (asumiendo que necesitas cargar desde una URL como antes)
        Glide.with(contexto)
            .load(imageUrl)
            .into(holder.imgproducto)

        holder.imgproducto.setOnClickListener {
            verDetalleProducto(producto)
        }
    }

    fun verDetalleProducto(producto: clsProductos) {
        // Se cambia el destino a VistaDetalleProducto (se asume que existe)
        val intent = Intent(contexto, producto_layout::class.java).apply {
            // Se pasan las claves de producto relevantes
            putExtra("producto_no_serie", producto.vchNo_Serie)
            putExtra("producto_nombre", producto.vchNombre)
            putExtra("producto_descripcion", producto.vchDescripcion)
            putExtra("producto_precio", producto.floPrecioUnitario)
            putExtra("producto_stock", producto.intStock)

            // Si la imagen es un String o Int, se pasa directamente
            putExtra("producto_imagen", producto.vchImagen)
        }
        contexto.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return listaproductos.size
    }

}