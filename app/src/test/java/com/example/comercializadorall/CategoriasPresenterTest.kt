import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CategoriasRepository
import com.example.comercializadorall.Presentador.CategoriasPresenter
import com.example.comercializadorall.Vista.Contracts.CategoriasContract
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.* import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriasPresenterTest {

    //Instancias a simular (Mocks)
    private lateinit var mockView: CategoriasContract.View
    private lateinit var mockRepository: CategoriasRepository

    //Instancia a probar
    private lateinit var presenter: CategoriasPresenter

    // Datos de prueba
    private val productoDummy = clsProductos(
        vchNo_Serie = "P123",
        vchNombre = "Categoría Test",
        vchDescripcion = "Desc",
        floPrecioUnitario = 10.50f,
        intStock = 100,
        vchImagen = "url",
        vchMarca = "Marca",
        vchCategoria = "Cat",
        vchCobertura = "Cob"
    )
    private val listaProductosDummy = listOf(productoDummy)

    @Before
    fun setUp() {
        mockView = mock()
        mockRepository = mock()

        presenter = CategoriasPresenter(mockView, mockRepository)
    }

    // Simula una llamada a la API exitosa (para List<clsProductos>)
    private fun mockCallSuccess(data: List<clsProductos>): Call<List<clsProductos>> {
        val mockCall = mock(Call::class.java) as Call<List<clsProductos>>
        // Usar any(Callback::class.java) o una importación de Mockito-Kotlin si es necesario
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<clsProductos>>
            callback.onResponse(mockCall, Response.success(data))
            null
        }.`when`(mockCall).enqueue(any<Callback<List<clsProductos>>>())
        return mockCall
    }
    // Simula una llamada a la API fallida
    private fun mockCallFailure(t: Throwable): Call<List<clsProductos>> {
        val mockCall = mock(Call::class.java) as Call<List<clsProductos>>
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<clsProductos>>
            callback.onFailure(mockCall, t)
            null
        }.`when`(mockCall).enqueue(any<Callback<List<clsProductos>>>())
        return mockCall
    }
    // Simula una llamada a la API fallida (HTTP Error para List<clsProductos>)
    private fun mockCallHttpError(code: Int): Call<List<clsProductos>> {
        val mockCall = mock(Call::class.java) as Call<List<clsProductos>>
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<clsProductos>>
            callback.onResponse(mockCall, Response.error(code, mock(okhttp3.ResponseBody::class.java)))
            null
        }.`when`(mockCall).enqueue(any<Callback<List<clsProductos>>>())
        return mockCall
    }
    // Simula una llamada a la API para un solo producto (clsProductos)
    private fun mockSingleCallSuccess(data: clsProductos?): Call<clsProductos> {
        val mockCall = mock(Call::class.java) as Call<clsProductos>
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<clsProductos>
            callback.onResponse(mockCall, Response.success(data))
            null
        }.`when`(mockCall).enqueue(any<Callback<clsProductos>>())
        return mockCall
    }
    // Simula una llamada a la API de un solo producto con error HTTP
    private fun mockSingleCallHttpError(code: Int): Call<clsProductos> {
        val mockCall = mock(Call::class.java) as Call<clsProductos>
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<clsProductos>
            callback.onResponse(mockCall, Response.error(code, mock(okhttp3.ResponseBody::class.java)))
            null
        }.`when`(mockCall).enqueue(any<Callback<clsProductos>>())
        return mockCall
    }

    //PRUEBAS PARA iniciar() (Carga Inicial)
    @Test
    fun iniciar_cargaExitosa_muestraCategorias() {
        `when`(mockRepository.obtenerCategoriasIniciales()).thenReturn(mockCallSuccess(listaProductosDummy))
        presenter.iniciar()

        verify(mockView).mostrarCargando(true)
        verify(mockView).mostrarCargando(false)
        verify(mockView).mostrarCategorias(listaProductosDummy)
        verify(mockView, never()).mostrarMensajeError(anyString())
    }

    @Test
    fun iniciar_cargaVacia_muestraError() {
        `when`(mockRepository.obtenerCategoriasIniciales()).thenReturn(mockCallSuccess(emptyList()))
        presenter.iniciar()

        verify(mockView).mostrarCargando(true)
        verify(mockView).mostrarCargando(false)
        verify(mockView).mostrarMensajeError("Error: No hay categorías disponibles.")
        verify(mockView, never()).mostrarCategorias(any())
    }
}