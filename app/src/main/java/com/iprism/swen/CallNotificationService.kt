package com.iprism.swen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.iprism.swen.activities.HomeActivity
import com.iprism.swen.videocalling.ReceiveVideoConferenceActivity
import kotlin.random.Random

class CallNotificationService : Service() {

    private val CHANNEL_ID = "CALL_NOTIFICATION_CHANNEL"
    var action = ""
    var doctorName : String? = null
    var token : String? = null
    var doctorId : String? = null
    var playerId : String? = null
    var consultType : String? = null
    var familyMemberId : String? = null
    var bookingId : String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.hasExtra("action")) {
            action = intent.getStringExtra("action")!!
        }
        Log.d("actionCall", action)
        if (intent.hasExtra("doctorName")) {
            doctorName = intent.getStringExtra("doctorName")
            Log.d("doctorName", doctorName.toString())
        }
        if (intent.hasExtra("token")) {
            token = intent.getStringExtra("token")
        }
        if (intent.hasExtra("doctorId")) {
            doctorId = intent.getStringExtra("doctorId")
        }
        if (intent.hasExtra("playerId")) {
            playerId = intent.getStringExtra("playerId")
        }
        if (intent.hasExtra("consultType")) {
            consultType = intent.getStringExtra("consultType")
        }
        if (intent.hasExtra("bookingId")) {
            bookingId = intent.getStringExtra("bookingId")
        }
        if (intent.hasExtra("familyMemberId")) {
            familyMemberId = intent.getStringExtra("familyMemberId")
        }
        if (action == "start_call") {
            showNotification(doctorName!!, "Incoming Call")
        } else if (action == "started_call") {
            updateNotification(doctorName!!, "You have an Ongoing Call")
            stopVibration(applicationContext)
        } else if (action == "missed_call") {
            /*updateNotification(userName!!, "Missed Call")
            stopVibration(applicationContext)*/
            stopVibration(applicationContext)
            Log.d("reject", "rejected")
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
            stopForeground(STOP_FOREGROUND_REMOVE) // Stop the foreground service
            stopSelf()
        } else if (action == "accept_call") {
            stopVibration(applicationContext)
            val notificationIntent = Intent(this, ReceiveVideoConferenceActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            notificationIntent.putExtra("token", token)
            notificationIntent.putExtra("callType", "high")
            notificationIntent.putExtra("name", doctorName)
            notificationIntent.putExtra("doctorId", doctorId)
            notificationIntent.putExtra("playerId", playerId)
            notificationIntent.putExtra("consultType", consultType)
            notificationIntent.putExtra("bookingId", bookingId)
            notificationIntent.putExtra("familyMemberId", familyMemberId)
            startActivity(notificationIntent)
        } else if (action == "reject_call") {
            stopVibration(applicationContext)
            Log.d("reject", "rejected")
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
            stopForeground(STOP_FOREGROUND_REMOVE) // Stop the foreground service
            stopSelf() // Stop the service
            /*val intent = Intent(this, ApiForegroundService::class.java)
            ContextCompat.startForegroundService(this, intent)*/
        } else if (action == "hang_up") {
            stopVibration(applicationContext)
            Log.d("reject", "rejected")
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
            stopForeground(STOP_FOREGROUND_REMOVE) // Stop the foreground service
            stopSelf() // Stop the service
            disconnectCall()
        }
        return START_STICKY
    }

    private fun showNotification(title: String, message: String) {
        createNotificationChannel()
        val acceptIntent = Intent(this, ReceiveVideoConferenceActivity::class.java).apply {
            putExtra("action", "accept_call")
            putExtra("callType", "high")
            putExtra("token", token)
            putExtra("name", doctorName)
            putExtra("doctorId", doctorId)
            putExtra("playerId", playerId)
            putExtra("consultType", consultType)
            putExtra("bookingId", bookingId)
            putExtra("familyMemberId", familyMemberId)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val acceptRequestCode = Random.nextInt(1, 10000)
        val acceptPendingIntent = PendingIntent.getActivity(this, acceptRequestCode, acceptIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Intent for "Reject" button
        val rejectIntent = Intent(this, ReceiveVideoConferenceActivity::class.java).apply {
            putExtra("token", token)
            putExtra("name", doctorName)
            putExtra("callType", "reject")
            putExtra("doctorId", doctorId)
            putExtra("playerId", playerId)
            putExtra("consultType", consultType)
            putExtra("bookingId", bookingId)
            putExtra("familyMemberId", familyMemberId)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val rejectRequestCode = Random.nextInt(10000, 100000)
        val rejectPendingIntent = PendingIntent.getActivity(
            this,
            rejectRequestCode,
            rejectIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationIntent = Intent(this, ReceiveVideoConferenceActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("token", token)
            putExtra("name", doctorName)
            putExtra("doctorId", doctorId)
            putExtra("playerId", playerId)
            putExtra("consultType", consultType)
            putExtra("bookingId", bookingId)
            putExtra("familyMemberId", familyMemberId)
            putExtra("callType", "low")
        }
        val pendingRequestCode = Random.nextInt(100000, 1000000)
        val pendingIntent = PendingIntent.getActivity(
            this,
            pendingRequestCode,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenIntent = Intent(this, ReceiveVideoConferenceActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("token", token)
            putExtra("name", doctorName)
            putExtra("doctorId", doctorId)
            putExtra("playerId", playerId)
            putExtra("consultType", consultType)
            putExtra("bookingId", bookingId)
            putExtra("familyMemberId", familyMemberId)
            putExtra("callType", "low")
        }
        val fullScreenRequestCode = Random.nextInt(1000000, 10000000)
        val fullScreenIntent1 = PendingIntent.getActivity(
            this,
            fullScreenRequestCode,
            fullScreenIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("requestCode", "$acceptRequestCode, $rejectRequestCode, $pendingRequestCode, $fullScreenRequestCode")
        /*val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo1) // Replace with your app icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .addAction(
                R.drawable.incoming_call_img,
                "Accept",
                acceptPendingIntent
            ) // Accept button
            .addAction(R.drawable.missed_call_img, "Reject", rejectPendingIntent)
            .setContentIntent(pendingIntent) // Attach the PendingIntent
            .setAutoCancel(true) // Dismiss notification when clicked
            .setOngoing(true) // Makes the notification non-dismissible
            .setFullScreenIntent(pendingIntent, true)
            .build()
        startForeground(1, notification)*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val person = Person.Builder()
                .setName(title)
                .build()

            // Create the CallStyle notification
            val callStyle = NotificationCompat.CallStyle.forScreeningCall(person, rejectPendingIntent, acceptPendingIntent)

            val notification12 = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.med_connect_logo) // Replace with your app icon
                .setStyle(callStyle)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setAutoCancel(true)
                .setFullScreenIntent(fullScreenIntent1, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
            startForeground(1, notification12)
        }
        else {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.med_connect_logo) // Replace with your app icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .addAction(
                    R.drawable.incoming_call_img,
                    "Accept",
                    acceptPendingIntent
                ) // Accept button
                .addAction(R.drawable.missed_call_img, "Reject", rejectPendingIntent)
                .setContentIntent(pendingIntent) // Attach the PendingIntent
                .setAutoCancel(true) // Dismiss notification when clicked
                .setOngoing(true) // Makes the notification non-dismissible
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(fullScreenIntent1, true)
                .build()
            startForeground(1, notification)
        }
    }

    private fun updateNotification(title: String, message: String) {
        Log.d("status", "updated")
        stopForeground(true)
        var intent: Intent? = null
        if (message.equals("Missed Call", true)) {
            intent = Intent(applicationContext, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            intent = Intent(applicationContext, ReceiveVideoConferenceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val person = android.app.Person.Builder()
                .setName(title)
                .build()
            val intent1 = Intent(applicationContext, VideoConferenceActivity2::class.java).apply {
                putExtra("callType", "hang_up")
                putExtra("token", token)
                putExtra("userName", userName)
                putExtra("userId", userId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val hangupIntent = PendingIntent.getActivity(
                applicationContext,
                1, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val builder = Notification.Builder(applicationContext, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.logo1)
                .setStyle(
                    Notification.CallStyle.forOngoingCall(person, hangupIntent))
                .build()
            startForeground(1, builder)
        } else {
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo1) // Replace with your app icon
                .build()
            notificationManager.notify(1, notification) // Removes the foreground flag
            stopSelf() // Stops the service
        }*/
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.med_connect_logo) // Replace with your app icon
            .build()
        notificationManager.notify(1, notification) // Removes the foreground flag
        stopSelf() // Stops the service
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Call Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?) = null

    private fun disconnectCall() {
        //showProgress(binding.progress)
        /*val khamHaiNaApi = KaamHaiNaApi()
        val khamHaiNaService = khamHaiNaApi.createKhamHaiNaService()
        val disconnectCallRequest = DisconnectCallRequest(userId!!.toInt(), User(this).getUserDetails()[User.ID]!!.toInt(), "")
        val call: Call<DisconnectCallApiResponse> = khamHaiNaService!!.discoonectCall(disconnectCallRequest)
        Log.d("disconnectCallRequest", disconnectCallRequest.toString())
        call.enqueue(object : Callback<DisconnectCallApiResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DisconnectCallApiResponse?>, response: Response<DisconnectCallApiResponse?>){
                //hideProgress(binding.progress)
                if (response.isSuccessful) {
                    val getTokenApiResponse: DisconnectCallApiResponse? = response.body()
                    if (getTokenApiResponse!!.status) {
                    } else {

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<DisconnectCallApiResponse?>, t: Throwable) {
                //hideProgress(binding.progress)
            }
        })*/
    }

    private fun stopVibration(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.cancel() // Stop any ongoing vibration
    }
}
