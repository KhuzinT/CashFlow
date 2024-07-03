package ru.khuzintka.cashflow.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ru.khuzintka.cashflow.R

const val notificationID = 121
const val channelID = "channel1"
const val titleExtra = "titleExtra"

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.splash_icon)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}