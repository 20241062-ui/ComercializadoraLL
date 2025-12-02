package com.example.comercializadorall.Modelo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.comercializadorall.Vista.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_Service"
    private val CHANNEL_ID = "MyNotificationChannelID"

    // ----------------------------------------------------
    // I. OBTENCIÓN DEL TOKEN
    // ----------------------------------------------------
    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo token de registro: $token")
        sendRegistrationToServer(token)
    }

    // ----------------------------------------------------
    // II. RECEPCIÓN DEL MENSAJE
    // ----------------------------------------------------
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // 1. REVISAR PRIMERO SI HAY DATA (Esto funciona siempre: app abierta o cerrada)
        if (remoteMessage.data.isNotEmpty()) {
            val titulo = remoteMessage.data["title"] ?: "Sin título"
            val mensaje = remoteMessage.data["body"] ?: "Nuevo mensaje"
            // Si envías un campo "count" desde el servidor, úsalo; si no, pon 1
            val badgeCount = remoteMessage.data["count"]?.toIntOrNull() ?: 1

            sendNotification(titulo, mensaje, badgeCount)
        }

        // 2. SI NO HAY DATA, REVISAR SI ES NOTIFICACIÓN (Solo funcionará con App abierta)
        else {
            remoteMessage.notification?.let {
                sendNotification(it.title, it.body, 1)
            }
        }
    }

    // ----------------------------------------------------
    // III. CONSTRUCCIÓN DE LA NOTIFICACIÓN (REFACTORIZADO)
    // ----------------------------------------------------
    private fun sendNotification(title: String?, body: String?, number: Int) {

        // A. PREPARAR EL INTENT (Para abrir la App)
        val intent = Intent(this, MainActivity::class.java).apply {
            // Limpia la pila de actividades para que no se amontonen
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // B. CREAR EL PENDING INTENT
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            // FLAG_IMMUTABLE es requerido en Android 12+ (SDK 31)
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // C. CREAR CANAL (Con soporte para Badge)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Notificaciones Generales"
            val channelDescription = "Alertas importantes de la aplicación"
            // IMPORTANCE_HIGH hace que suene y vibre
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
                setShowBadge(true) // <--- ESTO ACTIVA EL PUNTO/CONTADOR
            }
            notificationManager.createNotificationChannel(channel)
        }

        // D. CONSTRUIR LA NOTIFICACIÓN VISUAL
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // CAMBIA esto por R.drawable.tu_logo
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // Desaparece al tocarla
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // <--- Aquí vinculamos el click con abrir la App
            .setNumber(number) // <--- Intenta poner el número (depende del launcher)
        // Opcional: Color del led o acento
        // .setColor(ContextCompat.getColor(this, R.color.tu_color_primario))

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    // ----------------------------------------------------
    // IV. LÓGICA DE BACKEND
    // ----------------------------------------------------
    private fun sendRegistrationToServer(token: String) {
        // TODO: Enviar token al servidor de la comercializadora
    }
}