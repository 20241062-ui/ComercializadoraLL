import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.Presentador.LoginPresenter
import com.example.comercializadorall.Vista.Contracts.LoginContract
import com.example.comercializadorall.Vista.clsDatosRespuesta
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LoginPresenterTest {

    //Instancias a simular (Mocks)
    private lateinit var mockVista: LoginContract
    private lateinit var mockModelo: LoginModel

    //Instancia a probar
    private lateinit var presentador: LoginPresenter

    //se ejecuta antes de cada prueba
    @Before
    fun setUp() {
        // Inicializar las instancias simuladas
        mockVista = mock(LoginContract::class.java)
        mockModelo = mock(LoginModel::class.java)

        // Crear el Presenter con las dependencias simuladas
        presentador = LoginPresenter(mockVista, mockModelo)
    }

    // ESCENARIOS DE VALIDACIÓN LOCAL
    @Test
    fun iniciarSesion_camposVacios_muestraMensajeError() {

        // Llamar al metodo campos vacios
        presentador.iniciarSesion("", "password123")
        presentador.iniciarSesion("correo@test.com", "")
        presentador.iniciarSesion("", "")

        //Verificar que se mostró el mensaje de error *sin llamar al modelo
        verify(mockVista, times(3)).mostrarMensaje("Debe llenar todos los campos")
        verify(mockModelo, never()).iniciarSesion(anyString(), anyString(), any())
    }

    //ESCENARIOS DE INTERACCIÓN CON LA API (SIMULADA)

    @Test
    fun iniciarSesion_credencialesCorrectas_navegaAMain() {
        //Configurar el Mock del Modelo para simular una respuesta exitosa de la API
        val respuestaExitosa = listOf(clsDatosRespuesta("Correcto", "Bienvenido", user_id = 1))

        // Simular que el modelo responde con éxito a la llamada API
        `when`(mockModelo.iniciarSesion(anyString(), anyString(), any())).thenAnswer { invocation ->
            // Capturamos el callback y lo ejecutamos con los datos simulados
            val callback = invocation.arguments[2] as (List<clsDatosRespuesta>?, String?) -> Unit
            callback(respuestaExitosa, null)
        }
        presentador.iniciarSesion("correo_ok@test.com", "password_ok")

        //Verificar que el modelo fue llamado y se ejecutó la navegación
        verify(mockModelo).iniciarSesion(eq("correo_ok@test.com"), eq("password_ok"), any())
        verify(mockVista).navegarAMain()
        verify(mockVista, never()).mostrarMensaje(anyString())
    }

    @Test
    fun iniciarSesion_credencialesIncorrectas_muestraMensajeError() {
        // Configurar el Mock del Modelo para simular una respuesta fallida de la API
        val respuestaFallida = listOf(clsDatosRespuesta("Incorrecto", "Credenciales invalidas"))

        // Simular que el modelo responde con datos de error
        `when`(mockModelo.iniciarSesion(anyString(), anyString(), any())).thenAnswer { invocation ->
            val callback = invocation.arguments[2] as (List<clsDatosRespuesta>?, String?) -> Unit
            callback(respuestaFallida, null)
        }
        presentador.iniciarSesion("error@test.com", "bad_password")

        // Verificar el mensaje de error
        verify(mockVista).mostrarMensaje("Usuario o contraseña incorrectos")
        verify(mockVista, never()).navegarAMain()
    }

    @Test
    fun iniciarSesion_errorConexionApi_muestraMensajeDeError() {
        //simular un error de conexión
        val errorDeConexion = "Error en la conexión: Timeout"

        // Simular que el modelo responde con un error de conexión
        `when`(mockModelo.iniciarSesion(anyString(), anyString(), any())).thenAnswer { invocation ->
            val callback = invocation.arguments[2] as (List<clsDatosRespuesta>?, String?) -> Unit
            callback(null, errorDeConexion)
        }
        presentador.iniciarSesion("error@test.com", "password")

        //Verificar que se mostró el mensaje de error
        verify(mockVista).mostrarMensaje(errorDeConexion)
        verify(mockVista, never()).navegarAMain()
    }
}