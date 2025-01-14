package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.sendNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var selectedFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name))
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        // TODO: Implement code below
        binding.content.customButton.setOnClickListener {
            binding.content.apply {
                customButton.buttonState = ButtonState.Clicked
                selectedFile =
                    when(radioGroup.checkedRadioButtonId) {
                        R.id.rbGlide -> "https://github.com/bumptech/glide"
                        R.id.rbGithub -> "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                        R.id.rbRetrofit -> "https://github.com/square/retrofit"
                        else -> ""
                    }
            }
            download(selectedFile)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                binding.content.customButton.buttonState = ButtonState.Completed
                notificationManager.sendNotification(
                    selectedFile,
                    downloadStatus(id),
                    context!!)
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    private fun downloadStatus(id: Long?): String {
        var result = "Failed"
        val query = DownloadManager.Query()
        query.setFilterById(id!!)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            if (DownloadManager.STATUS_SUCCESSFUL == status) {
                result = "Success"
            }
        }
        return result
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )


            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "File download status"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun download(url:String) {
        if (url.isEmpty()) {
            Toast.makeText(this, "Please select a file to download", Toast.LENGTH_LONG).show()
            return
        }
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        binding.content.customButton.buttonState = ButtonState.Loading
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}