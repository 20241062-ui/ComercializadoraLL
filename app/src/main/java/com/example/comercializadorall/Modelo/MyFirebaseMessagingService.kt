package com.example.comercializadorall.Modelo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_Service"
    private val CHANNEL_ID = "MyNotificationChannelID" // ID único para el canal

    // ----------------------------------------------------
    // I. OBTENCIÓN DEL TOKEN
    // ----------------------------------------------------

    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo token de registro: $token")
        // Aquí debes enviar el token a tu servidor backend
        sendRegistrationToServer(token)
    }

    // ----------------------------------------------------
    // II. RECEPCIÓN DEL MENSAJE (APP EN PRIMER PLANO)
    // ----------------------------------------------------

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Mensaje recibido de: ${remoteMessage.from}")

        // Procesamos la carga de la notificación si existe
        remoteMessage.notification?.let {
            Log.d(TAG, "Notificación: ${it.title} - ${it.body}")
            sendNotification(it.title, it.body)
        }

        // Puedes procesar la carga de 'data' si existe (para mensajes de datos)
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Carga de datos: ${remoteMessage.data}")
            // Aquí puedes manejar datos personalizados si no envías una notificación estándar.
        }
    }

    // ----------------------------------------------------
    // III. CONSTRUCCIÓN DE LA NOTIFICACIÓN
    // ----------------------------------------------------

    private fun sendNotification(title: String?, body: String?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación (obligatorio en Android 8.0+)
        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Usa un icono de tu proyecto
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Mostrar la notificación con un ID único
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Notificaciones Generales"
        val channelDescription = "Alertas importantes de la aplicación"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
    }

    // ----------------------------------------------------
    // IV. LÓGICA DE BACKEND (Pendiente)
    // ----------------------------------------------------

    private fun sendRegistrationToServer(token: String) {
        // En un proyecto real, esta función envía el token a tu servidor.
        // Por ahora, solo es una marca de posición.
    }
}