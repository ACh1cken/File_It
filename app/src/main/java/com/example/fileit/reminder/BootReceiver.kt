package com.example.fileit.reminder

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.fileit.R
import java.util.*


const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class BootReceiver : BroadcastReceiver() {
    //    var alarm: Alarm = Alarm()


//    override fun onReceive(context: Context?, intent: Intent) {
//        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
//            val notificationManager =
//                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            val notification: Notification = Notification.Builder(context)
//                .setContentTitle("Random title")
//                .setContentText("Random text")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(
//                    PendingIntent.getActivity(
//                        context,
//                        0,
//                        Intent(context, MainPage::class.java),
//                        0
//                    )
//                )
//                .build()
//
//            notificationManager.notify(0, notification)
//        }
//    }
    override fun onReceive(context: Context, intent: Intent) {
//  if (intent.action == Intent.ACTION_BOOT_COMPLETED){
//
//  }
    Log.e("Broadcast","Broadcast Received")
    val notification = NotificationCompat.Builder(context, channelID)
        .setSmallIcon(R.drawable.fileit_logo)
        .setContentTitle(intent.getStringExtra(titleExtra))
        .setContentText(intent.getStringExtra(messageExtra))
        .build()

    val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.notify(notificationID, notification)

    }


//    fun rescheduleAlarm(context: Context){
//        val i = Intent(ctxt, ScheduledService::class.java)
//        val pi = PendingIntent.getService(ctxt, 0, i, 0)
//
//        ctxt.getSystemService(Context.ALARM_SERVICE).setRepeating(
//            AlarmManager.ELAPSED_REALTIME,
//            SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi
//        )
//    }
}