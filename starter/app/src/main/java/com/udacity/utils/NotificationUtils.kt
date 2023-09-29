package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(filename:String, status:String, context: Context) {
    val contentIntent = Intent(context, DetailActivity::class.java)
    contentIntent.putExtra(context.getString(R.string.notification_extra_filename), filename)
    contentIntent.putExtra(context.getString(R.string.notification_extra_status), status)
    val pendingIntent = PendingIntent.getActivity(context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp,
            context.getString(R.string.notification_button),
            pendingIntent)

    notify(NOTIFICATION_ID, builder.build())
}