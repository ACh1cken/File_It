package com.example.fileit.reminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fileit.R


const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//    Log.e("Broadcast","Broadcast Received")
    val notification = NotificationCompat.Builder(context, channelID)
        .setSmallIcon(R.drawable.fileit_logo)
        .setContentTitle(intent.getStringExtra(titleExtra))
        .setContentText(intent.getStringExtra(messageExtra))
        .build()

    val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(notificationID, notification)

    }


}