package com.example.comercializadorall

import com.example.comercializadorall.Modelo.LoginModel
import com.example.comercializadorall.Presentador.LoginPresenter
import com.example.comercializadorall.Vista.Contracts.LoginContract
import com.example.comercializadorall.Vista.clsDatosRespuesta
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.eq

class LoginPresenterTest {
    // 1. Declarar los Mocks para la Vista y el Modelo
    private val mockVista: LoginContract = mock() // Simula el Activity/Fragment
    private val mockModel: LoginModel = mock()   // Simula la llamada a la API

    // 2. Instancia real del Presenter, inyectando los Mocks
    private lateinit var presenter: LoginPresenter

    @Before
    fun setup() {
        presenter = LoginPresenter(mockVista, mockModel)
    }

    // --- Caso 1: Login Exitoso ---
    @Test
    fun iniciarSesion_successfulResponse_navigatesToMain() {
        // ARRANGE: Decirle al Mock del Model que responda con éxito
        doAnswer { invocation ->
            val callback = invocation.getArgument<(List<clsDatosRespuesta>?, String?) -> Unit>(2)

            // CORRECCIÓN APLICADA AQUÍ: Se usa "Correcto" y el user_id es un Int
            val successfulList = listOf(clsDatosRespuesta("Correcto", "Login exitoso", 42))

            callback.invoke(successfulList, null)
            null
        }.`when`(mockModel).iniciarSesion(any(), any(), any())

        // ACT y ASSERT (Se mantienen los nombres de funciones ya corregidos: iniciarSesion y navegarAMain)
        presenter.iniciarSesion("a@b.com", "12345")

        verify(mockVista).navegarAMain()
        verify(mockVista, never()).mostrarMensaje(any())
    }

    // --- Caso 2: Login Fallido (Error de servidor) ---
    @Test
    fun login_errorModelResponse_showsErrorMessage() {
        // ARRANGE
        val expectedError = "Credenciales incorrectas"

        // 1. Configurar el Mock del Model para FALLAR
        doAnswer { invocation ->
            // Simular la respuesta fallida
            val callback = invocation.getArgument<(List<clsDatosRespuesta>?, String?) -> Unit>(2)
            callback.invoke(null, expectedError) // Pasa null en datos y el error
            null
        }.`when`(mockModel).iniciarSesion(any(), any(), any())

        // ACT
        presenter.iniciarSesion("user", "pass")

        // ASSERT
        // 2. Verificar que la VISTA fue llamada para mostrar el error esperado
        verify(mockVista).mostrarMensaje(expectedError)
        // Y que no haya intentado navegar a Home
        verify(mockVista, never()).navegarAMain()
    }
}