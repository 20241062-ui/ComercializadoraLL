package com.example.comercializadorall.Presentador

import com.example.comercializadorall.Modelo.clsInformacion
import com.example.comercializadorall.Vista.Contracts.EmpresaContract
import com.example.comercializadorall.Vista.InformaciondelaEmpresa

class EmpresaPresenter(private val view: InformaciondelaEmpresa) : EmpresaContract.Presenter {
    override fun cargarDatos() {

        val misionEstatica = clsInformacion(
            intid = 1,
            vchtitulo = "Misión",
            vchcontenido = "Desarrollar herramientas digitales innovadoras que permitan a los negocios gestionar sus procesos de venta e inventario de forma rápida, confiable y sencilla, contribuyendo al crecimiento y eficiencia de cada empresa usuaria.",
        )

        val visionEstatica = clsInformacion(
            intid = 2,
            vchtitulo = "Visión",
            vchcontenido = "Convertirnos en la empresa líder en soluciones tecnológicas accesibles y de alta calidad, ofreciendo productos innovadores que impulsen el crecimiento, la eficiencia y la transformación digital de nuestros clientes.",
        )

        view.mostrarInformacion(misionEstatica, visionEstatica)
    }
}