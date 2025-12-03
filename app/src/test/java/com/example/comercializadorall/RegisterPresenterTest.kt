package com.example.comercializadorall
import com.example.comercializadorall.Modelo.RegistroModel
import com.example.comercializadorall.Presentador.RegistroPresenter
import com.example.comercializadorall.Vista.Contracts.RegistroContract


import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RegistroPresenterTest {

    // Declarar los Mocks: Las clases que el Presenter usa, pero que no queremos probar.
    private val mockVista: RegistroContract = mock()
    private val mockModel: RegistroModel = mock()

    private lateinit var presenter: RegistroPresenter

    @Before
    fun setup() {
        // Inicializar el Presenter inyectándole los Mocks
        presenter = RegistroPresenter(mockVista, mockModel)
    }

    @Test
    fun registro_successfulModelResponse_showsMessageAndNavigates() {
        val expectedMessage = "Usuario registrado con éxito."
        val fixedRol = "Cliente"

        // 1. Configurar el Mock del Model:
        // Usamos doAnswer para simular que el Model responde al callback onSuccess.
        doAnswer { invocation ->
            // El callback (OnRegistroListener) es el SEXTO argumento (0-indexado: nombre, apellido, correo, password, rolFijo, listener)
            val listener = invocation.getArgument<RegistroModel.OnRegistroListener>(5)
            // Ejecutamos el método de éxito del listener
            listener.onSuccess(expectedMessage)
            null
        }.`when`(mockModel).registrarUsuario(
            any(), any(), any(), any(), eq(fixedRol), any() // Verificamos que se pase el rol fijo "Cliente"
        )

        // ACT
        presenter.registrarUsuario("Juan", "Perez", "juan@test.com", "pass123")

        // ASSERT
        // 2. Verificar que la VISTA recibió la instrucción de mostrar el mensaje de éxito
        verify(mockVista,times(1)).mostrarMensaje(expectedMessage)
        // 3. Verificar que la VISTA recibió la instrucción de navegación/cierre
        verify(mockVista,times(1)).registroExitoso()
        // 4. Verificar que NO se llamó al fallo
        verify(mockVista, times(1)).mostrarMensaje(any())
    }

    // ----------------------------------------------------------------------
    // --- CASO 2: Registro Fallido (El Model llama a onFailure) ---
    // ----------------------------------------------------------------------
    @Test
    fun registro_failedModelResponse_showsErrorMessage() {
        // ARRANGE
        val expectedError = "El correo ya existe en el sistema."
        val fixedRol = "Cliente"

        // 1. Configurar el Mock del Model para FALLAR
        doAnswer { invocation ->
            // El listener es el SEXTO argumento
            val listener = invocation.getArgument<RegistroModel.OnRegistroListener>(5)
            // Ejecutamos el método de fallo del listener
            listener.onFailure(expectedError)
            null
        }.`when`(mockModel).registrarUsuario(
            any(), any(), any(), any(), eq(fixedRol), any()
        )

        // ACT
        presenter.registrarUsuario("Juan", "Perez", "juan@test.com", "pass123")

        // ASSERT
        // 2. Verificar que la VISTA recibió el mensaje de error esperado
        verify(mockVista).mostrarMensaje(expectedError)
        // 3. Verificar que NO se llamó a la navegación exitosa
        verify(mockVista, never()).registroExitoso()
    }
}