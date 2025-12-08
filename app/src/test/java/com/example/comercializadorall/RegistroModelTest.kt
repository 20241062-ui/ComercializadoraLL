import com.example.comercializadorall.Modelo.RegistroModel
import com.example.comercializadorall.Modelo.ifaceApiService
import com.example.comercializadorall.Vista.clsDatosRespuesta
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Response

class RegistroModelTest {
    // Instancia simulada de la interfaz de la API
    private lateinit var mockApiService: ifaceApiService
    // Instancia a probar
    private lateinit var model: RegistroModel
    // Listener simula para verificar las llamadas onSuccess/onFailure
    private lateinit var mockListener: RegistroModel.OnRegistroListener

    @Before
    fun setUp() {
        mockApiService = mock(ifaceApiService::class.java)
        mockListener = mock(RegistroModel.OnRegistroListener::class.java)
        model = RegistroModel(mockApiService)
    }

    //PRUEBAS DE VALIDACIÓN DE LADO DEL CLIENTE (SIN LLAMADA A LA API)
    @Test
    fun registro_camposVacios_llamaOnFailure() {
        //Intentar registrar con el nombre vacío
        model.registrarUsuario("", "Perez", "correo@test.com", "pass1234", "Cliente", mockListener)
        //Verifica que el Listener recibió el error de campos obligatorios
        verify(mockListener).onFailure("Todos los campos (nombre, apellido, correo y contraseña) son obligatorios.")
        verify(mockApiService, never()).registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())
    }
    @Test
    fun registro_nombreOApellidoConNumeros_llamaOnFailure() {
        //Intenta registrar con números en el nombre
        model.registrarUsuario("J4vier", "Perez", "correo@test.com", "pass1234", "Cliente", mockListener)
        //Verificar el error de formato
        verify(mockListener).onFailure("El nombre y el apellido no deben contener números.")
        verify(mockApiService, never()).registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())
    }
    @Test
    fun registro_emailInvalido_llamaOnFailure() {
        //Intenta registrar con un formato de email incorrecto
        model.registrarUsuario("Javier", "Perez", "correo_invalido.com", "pass1234", "Cliente", mockListener)
        //Verifica el error de formato de email
        verify(mockListener).onFailure("El formato del correo electrónico es inválido. Por favor, revísalo.")
        verify(mockApiService, never()).registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())
    }
    @Test
    fun registro_passwordCorta_llamaOnFailure() {
        //Intenta registrar con una contraseña menor a 8 caracteres
        model.registrarUsuario("Javier", "Perez", "correo@test.com", "pass123", "Cliente", mockListener)
        //Verifica el error de longitud
        verify(mockListener).onFailure("La contraseña debe tener al menos 8 caracteres para ser segura.")
        verify(mockApiService, never()).registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())
    }

    //PRUEBAS DE INTERACCIÓN CON LA API (RESPUESTAS SIMULADAS)
    @Test
    fun registro_respuestaApiExitosa_llamaOnSuccess() {
        //Simula la respuesta exitosa de la API
        val mensajeExito = "Usuario registrado correctamente"
        val respuestaApi = listOf(clsDatosRespuesta(Estado = "true", Salida = mensajeExito))

        // Simula la llamada de Retrofit (debe usarse Mockito)
        val mockCall = mock(Call::class.java) as Call<List<clsDatosRespuesta>>
        `when`(mockApiService.registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(mockCall)

        // Simula la respuesta del metodo enqueue de Retrofit
        doAnswer { invocation ->
            val callback = invocation.arguments[0] as retrofit2.Callback<List<clsDatosRespuesta>>
            callback.onResponse(mockCall, Response.success(respuestaApi))
            null
        }.`when`(mockCall).enqueue(any())

        // Llama al registro con datos válidos
        model.registrarUsuario("Javier", "Perez", "correo@ok.com", "password1234", "Cliente", mockListener)

        //Verifica que la llamada a la API fue hecha y se llamó a onSuccess
        verify(mockApiService).registrarUsuario(eq("registrar"), eq("Javier"), eq("Perez"), eq("correo@ok.com"), eq("password1234"))
        verify(mockListener).onSuccess(mensajeExito)
        verify(mockListener, never()).onFailure(anyString())
    }

    @Test
    fun registro_correoYaExistenteApi_llamaOnFailure() {
        // Arrange: Simular la respuesta de la API cuando el correo ya está registrado
        val mensajeErrorApi = "El correo ya está registrado."
        val respuestaApi = listOf(clsDatosRespuesta(Estado = "false", Salida = mensajeErrorApi))

        val mockCall = mock(Call::class.java) as Call<List<clsDatosRespuesta>>
        `when`(mockApiService.registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as retrofit2.Callback<List<clsDatosRespuesta>>
            callback.onResponse(mockCall, Response.success(respuestaApi))
            null
        }.`when`(mockCall).enqueue(any())
        model.registrarUsuario("Javier", "Perez", "correo@existente.com", "password1234", "Cliente", mockListener)

        //Verifica que se llamó a onFailure con el mensaje de la API
        verify(mockListener).onFailure(mensajeErrorApi)
        verify(mockListener, never()).onSuccess(anyString())
    }
}