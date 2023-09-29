package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.detailContent.apply {
            tvFilename.text = intent.getStringExtra(getString(R.string.notification_extra_filename))
            tvStatus.text = intent.getStringExtra(getString(R.string.notification_extra_status))
        }

        val intent = Intent(this, MainActivity::class.java)

        binding.detailContent.buttonOk.setOnClickListener {
            startActivity(intent)
        }

        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()
    }
}
