package com.example.comercializadorall.Adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.databinding.ItemCarritoBinding

class CarritoAdapter(private var lista: MutableList<clsProductos>) :
    RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]

        holder.binding.txtNombre.text = p.vchNombre
        holder.binding.txtMarca.text = p.vchMarca
        holder.binding.txtPrecio.text = "$${p.floPrecioUnitario}"

        Glide.with(holder.itemView.context)
            .load(p.vchImagen)
            .into(holder.binding.imgProducto)
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: MutableList<clsProductos>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}
