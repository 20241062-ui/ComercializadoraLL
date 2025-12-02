package com.example.comercializadorall.Vista.Adaptador

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.R
import com.example.comercializadorall.Vista.VistaDetalle

// 💡 CAMBIO CLAVE: El constructor ahora recibe una lambda (función)
// que se ejecutará cuando se haga clic en la imagen.
class ProductoAdaptador(
    private val contexto: Context,
    private val listaproductos: List<clsProductos>,
    private val onImageClickListener: (clsProductos) -> Unit // Lambda para manejar el clic
) : RecyclerView.Adapter<ProductoAdaptador.ProductoViewHolder>() {

    // --- ViewHolder ---
    class ProductoViewHolder(control: View): RecyclerView.ViewHolder(control){
        val txtnombre: TextView = control.findViewById(R.id.txtNombre)
        var imgproducto: ImageView = control.findViewById(R.id.imgProducto)
        val txtprecio: TextView = control.findViewById(R.id.txtPrecio)
        val txtstock: TextView = control.findViewById(R.id.txtStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto_layout, parent, false)
        return ProductoViewHolder(view)
    }

    // --- onBindViewHolder ---
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaproductos[position]

        // --- Lógica de URL y GLIDE ---
        val URL_BASE_IMAGENES = "http://comercializadorall.grupoctic.com/ComercializadoraLL/img/"
        val urlCompleta = URL_BASE_IMAGENES + producto.vchImagen

        holder.txtnombre.text = producto.vchNombre
        holder.txtprecio.text = "Precio Unitario: $ ${String.format("%.2f", producto.floPrecioUnitario)}"
        holder.txtstock.text = "Stock Disponible: " + producto.intStock.toString()

        Log.d("URL_FINAL_CARGA", "Intentando cargar: $urlCompleta")

        Glide.with(contexto)
            .load(urlCompleta) // URL de imagen
            .fitCenter()
            .into(holder.imgproducto)

        // --- Lógica de Click en la IMAGEN ---
        // 💡 PASO CLAVE: Asignar el listener SÓLO al ImageView
        holder.imgproducto.setOnClickListener {
            // Cuando se hace clic en la imagen, invocamos la lambda y pasamos el producto.
            onImageClickListener(producto)
        }

        // Opcional: Si no quieres que toda la tarjeta sea clickeable,
        // asegúrate de NO llamar a setOnClickListener en holder.itemView.
    }

    // --- getItemCount ---
    override fun getItemCount(): Int {
        return listaproductos.size
    }
}
