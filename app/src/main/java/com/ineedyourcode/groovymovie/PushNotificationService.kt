package com.ineedyourcode.groovymovie

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ineedyourcode.groovymovie.view.MainActivity

class PushNotificationService : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "Push_key"
        private const val PUSH_KEY_MESSAGE = "Message_key"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 13
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteData = remoteMessage.data

        if (remoteData.isNotEmpty()) {
            handleMessage(remoteData.toMap())
        }
    }

    private fun handleMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]

        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("INTENT_KEY", 1)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.groovy_logo)
                setContentTitle(title)
                setContentText(message)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Test channel"
        val descriptionText = "Channel for Firebase notification testing"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(p0: String) {
    }
}