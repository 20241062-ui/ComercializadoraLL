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

class ProductoAdapter(val contexto: Context, val listapeliculas:List<clsPeliculas>): RecyclerView.Adapter<peliculaAdaptador.peliculaViewHolder>() {
    class peliculaViewHolder(control: View): RecyclerView.ViewHolder(control){
        //aqui espesificamos los controles del layout
        val imgpelicula: ImageView =control.findViewById(R.id.imgFoto)
        val txtnombre: TextView =control.findViewById(R.id.txtNombre)
        val txtdescripcion: TextView =control.findViewById(R.id.txtDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): peliculaViewHolder {
        //Aqui haremos el vinculocon el layout
        val view= LayoutInflater.from(parent.context).inflate(R.layout.pelicula_layout,parent,false)
        return peliculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: peliculaViewHolder,position: Int) {

        val pelicula=listapeliculas[position]

        holder.txtnombre.text=pelicula.Nombre
        holder.txtdescripcion.text=pelicula.Descripcion

        Glide.with(contexto)
            .load("https://javier.grupoctic.com/Peliculas/img/" + pelicula.imagenPeli)
            .into(holder.imgpelicula)
        holder.imgpelicula.setOnClickListener {
            verdetalle(pelicula)
        }
    }
    fun verdetalle(  pelicula: clsPeliculas){
        val intent= Intent(contexto, VistaDetalle::class.java).apply{
            putExtra("pelicula_id",pelicula.idPelicula)
            putExtra("pelicula_nombre",pelicula.Nombre)
            putExtra("pelicula_descripcion",pelicula.Descripcion)
            putExtra("pelicula_sinopsis", pelicula.Sinopsis)
            putExtra("pelicula_imagen",pelicula.imagenPeli)
            putExtra("pelicula_video",pelicula.videoPeli)


        }
        contexto.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return listapeliculas.size
    }

}