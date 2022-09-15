package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.notifications.Goto.openActivity
import com.example.notifications.databinding.ActivityMainBinding
@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var receiver: BroadcastReceiver
    lateinit var binding: ActivityMainBinding

    private val CHANNEL_NAME = "notify"
    private val CHANNEL_ID = "id123"
    private  val NOTIFICATION_ID = 1

    private lateinit var notification: Notification
    private lateinit var nm:NotificationManagerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        setupNotifications()
        binding.button.setOnClickListener(this)
        binding.sendNotifications.setOnClickListener(this)
        receiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(this@MainActivity, "Hey I received", Toast.LENGTH_SHORT).show()
            }
        }
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(receiver,filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun setupNotifications(){
        val intentClick = Intent(this,SecondActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_MUTABLE)
        val intentReceiver = Intent(this, NotificationReceiver::class.java).apply {
            action = "ACTION_REPLY"
            putExtra("EXTRA_NOT_ID",1)
        }
        val pendingIntentReceiver = PendingIntent.getBroadcast(this,100,intentReceiver,PendingIntent
            .FLAG_MUTABLE)
        notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Hey Got Notification")
            .setContentText("This is secret to show all")
            .setSmallIcon(R.drawable.hand_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.hand_foreground,"Reply",pendingIntentReceiver)
            .build()
        nm = NotificationManagerCompat.from(this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager
                .IMPORTANCE_DEFAULT
        ).apply {
            lightColor = Color.GREEN
            enableLights(true)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.send_notifications -> nm.notify(NOTIFICATION_ID,notification)
            R.id.button -> openActivity(SecondActivity::class.java){
                putSerializable("Hello",SampleData("Hey i am object"))
            }
        }
    }
}