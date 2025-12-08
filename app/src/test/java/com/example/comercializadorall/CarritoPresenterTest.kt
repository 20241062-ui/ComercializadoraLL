import com.example.comercializadorall.Modelo.ICarritoModel
import com.example.comercializadorall.Modelo.clsProductos
import com.example.comercializadorall.Presentador.CarritoPresenter
import com.example.comercializadorall.Vista.Contracts.ICarritoView
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CarritoPresenterTest {

    // Instancias simuladas (Mocks)
    private lateinit var mockView: ICarritoView
    private lateinit var mockModel: ICarritoModel

    // Instancia a probar
    private lateinit var presenter: CarritoPresenter

    // Producto de prueba
    private val productoDummy = clsProductos(
        vchNo_Serie = "SN123",
        vchNombre = "Laptop Test",
        floPrecioUnitario = 1000.0f,
        vchMarca = "MarcaX",
        vchImagen = "url_imagen",
        intCantidadCarrito = 1
    )

    @Before
    fun setUp() {
        mockView = mock(ICarritoView::class.java)
        mockModel = mock(ICarritoModel::class.java)
        presenter = CarritoPresenter(mockView, mockModel)
    }

    //TEST PARA agregarAlCarrito
    @Test
    fun agregarAlCarrito_sesionIniciada_agregaProductoYmuestraMensaje() {
        `when`(mockModel.estaSesionIniciada()).thenReturn(true)
        presenter.agregarAlCarrito(productoDummy)
        verify(mockModel).agregarProducto(productoDummy)
        verify(mockView).mostrarMensaje("Producto agregado al carrito")
        verify(mockView, never()).navegarALogin()
    }
    @Test
    fun agregarAlCarrito_sesionNoIniciada_muestraMensajeDeErrorYNavega() {
        //Simula que la sesión NO ESTÁ iniciada
        `when`(mockModel.estaSesionIniciada()).thenReturn(false)
        presenter.agregarAlCarrito(productoDummy)
        verify(mockModel, never()).agregarProducto(any())
        verify(mockView).mostrarMensaje("Debes iniciar sesión para agregar productos al carrito.")
        verify(mockView).navegarALogin()
    }

    //TEST PARA cargarCarrito
    @Test
    fun cargarCarrito_modeloVacio_muestraListaVacia() {
        val listaVacia = mutableListOf<clsProductos>()
        `when`(mockModel.obtenerCarrito()).thenReturn(listaVacia)
        presenter.cargarCarrito()
        verify(mockView).mostrarCarrito(listaVacia)
    }
    @Test
    fun cargarCarrito_modeloConDatos_muestraListaCompleta() {
        val listaConProductos = mutableListOf(productoDummy, productoDummy.copy(vchNo_Serie = "SN456"))
        `when`(mockModel.obtenerCarrito()).thenReturn(listaConProductos)
        presenter.cargarCarrito()
        verify(mockView).mostrarCarrito(listaConProductos)
    }
}