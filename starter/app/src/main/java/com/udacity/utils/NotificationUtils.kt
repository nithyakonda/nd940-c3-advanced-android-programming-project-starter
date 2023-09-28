package com.udacity.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.udacity.R

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, builder.build())
}