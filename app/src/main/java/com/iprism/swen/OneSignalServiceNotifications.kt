package com.iprism.swen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import com.iprism.swen.activities.HomeActivity
import com.iprism.swen.videocalling.Listener
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OneSignalServiceNotifications : OneSignal.OSRemoteNotificationReceivedHandler{

    private val CHANNEL_ID = "CALL_NOTIFICATION_CHANNEL"
    private var notifManager: NotificationManager? = null
    private var mChannel: NotificationChannel? = null
    var m = 0
    var intent: Intent? = null

    override fun remoteNotificationReceived(context: Context, osNotificationReceivedEvent: OSNotificationReceivedEvent) {

        CoroutineScope(Dispatchers.IO).launch {
            val notification = osNotificationReceivedEvent.notification
            Log.d("notification", "Data" + notification.additionalData)
            Log.d("notification", "notidications$notification")
            OneSignal.setNotificationOpenedHandler { result ->
                val data = result.notification.additionalData
                val customData = data?.optString("key_name", "")

                val intent = Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("from", "notification")  // Pass custom data
                }
                context.startActivity(intent)
            }

            val data = notification.additionalData
            val status = data?.optString("status", "normal") ?: "normal"
            val token = data?.optString("token", "")
            val doctorName = data?.optString("doctor_name", "Unknown User")
            val doctorId = data?.optString("doctor_id", "")
            val playerId = data?.optString("player_id", "")
            val familyMemberId = data?.optString("family_member_id", "")
            val bookingId = data?.optString("booking_id", "")
            val consultType = data?.optString("consult_type", "")
            Log.d("PlayerId", playerId.toString())


            when (status.lowercase()) {
                "call" -> {
                    withContext(Dispatchers.Main) {
                        startSound(context) }
                    startService(context, CallNotificationService::class.java, "start_call", token, doctorName, doctorId, playerId, consultType, bookingId, familyMemberId)
                    wakeUpScreen(context)
                }

                "disconnect" -> {
                    withContext(Dispatchers.Main) {
                        val stopIntent = Intent(context, SoundService::class.java)
                        stopIntent.action = SoundService.ACTION_STOP
                        context.startService(stopIntent)
                    }
                    startService(
                        context,
                        CallNotificationService::class.java,
                        "reject_call",
                        token,
                        doctorName,
                        doctorId,
                        playerId,
                        consultType,
                        bookingId,
                        familyMemberId
                    )
                }

                "disconnect1" -> {
                    withContext(Dispatchers.Main) {
                        val stopIntent = Intent(context, SoundService::class.java)
                        stopIntent.action = SoundService.ACTION_STOP
                        context.startService(stopIntent)
                    }
                    withContext(Dispatchers.Main) {
                        val homeIntent = Intent(context, HomeActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(homeIntent)
                    }
                    startService(
                        context,
                        CallNotificationService::class.java,
                        "reject_call",
                        token,
                        doctorName,
                        doctorId,
                        playerId,
                        consultType,
                        bookingId,
                        familyMemberId
                    )
                }

                "missed_call" -> {
                    withContext(Dispatchers.Main) {
                        val stopIntent = Intent(context, SoundService::class.java)
                        stopIntent.action = SoundService.ACTION_STOP
                        context.startService(stopIntent)
                    }
                    if (Listener.disconnectListener != null) {
                        Listener.disconnectListener.response("missedCall")
                    }
                    startService(
                        context,
                        CallNotificationService::class.java,
                        "missed_call",
                        token,
                        doctorName,
                        doctorId,
                        playerId,
                        consultType,
                        bookingId,
                        familyMemberId
                    )
                }

                "missed_call1" -> withContext(Dispatchers.Main) {
                    /*showNotificationMessage(
                        context,
                        userName,
                        "Missed Call",
                        Intent(context, HomeActivity::class.java),
                        "normal"
                    )*/
                }

                else -> {
                    /*withContext(Dispatchers.Main) {
                        val homeIntent = Intent(context, HomeActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(homeIntent)
                    }*/
                }
            }
            playNotificationSound(status, context)
            osNotificationReceivedEvent.complete(notification)
        }

        /*if (status.equals("call", ignoreCase = true)) {
            startSound(context)
            val intent = Intent(context, CallNotificationService::class.java)
            intent.putExtra("action", "start_call")
            intent.putExtra("token", token)
            intent.putExtra("userName", userName)
            intent.putExtra("userId", userId)
            context.startService(intent)
        } else if (status.equals("normal", ignoreCase = true)) {
            intent = Intent(context, HomeActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //showNotificationMessage(context, "remoteMessage.notification!!.title", "remoteMessage.notification!!.body", intent, status)
        } else if (status.equals("disconnect", ignoreCase = true)) {
            val stopIntent = Intent(context, SoundService::class.java)
            stopIntent.action = SoundService.ACTION_STOP
            context.startService(stopIntent)
            *//*intent = Intent(context, HomeActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)*//*
            val intent = Intent(context, CallNotificationService::class.java)
            intent.putExtra("action", "reject_call")
            intent.putExtra("token", token)
            intent.putExtra("userName", userName)
            intent.putExtra("userId", userId)
            context.startService(intent)
            //showNotificationMessage(context, "remoteMessage.notification!!.title", "remoteMessage.notification!!.body", intent, "normal")
        } else if (status.equals("disconnect1", ignoreCase = true)) {
            Log.d("statusDisconnect", "disconnect1")
            val stopIntent = Intent(context, SoundService::class.java)
            stopIntent.action = SoundService.ACTION_STOP
            context.startService(stopIntent)
            intent = Intent(context, HomeActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            val intent = Intent(context, CallNotificationService::class.java)
            intent.putExtra("action", "reject_call")
            intent.putExtra("token", token)
            intent.putExtra("userName", userName)
            intent.putExtra("userId", userId)
            context.startService(intent)
            //showNotificationMessage(context, "remoteMessage.notification!!.title", "remoteMessage.notification!!.body", intent, "normal")
        } else if (status.equals("missed_call", ignoreCase = true)) {
            val stopIntent = Intent(context, SoundService::class.java)
            stopIntent.action = SoundService.ACTION_STOP
            context.startService(stopIntent)
            intent = Intent(context, HomeActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            //showNotificationMessage(context, "remoteMessage.notification!!.title", "remoteMessage.notification!!.body", intent, "normal")
            val intent = Intent(context, CallNotificationService::class.java)
            intent.putExtra("action", "missed_call")
            intent.putExtra("token", token)
            intent.putExtra("userName", userName)
            intent.putExtra("userId", userId)
            context.startService(intent)
        }  else if (status.equals("missed_call1", ignoreCase = true)) {
            showNotificationMessage(context, userName, "Missed Call", intent, "normal")
        } else {
            intent = Intent(context, HomeActivity::class.java)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            *//*showNotificationMessage(context, "remoteMessage.notification!!.title", "remoteMessage.notification!!.body", intent, "normal")*//*
        }

        playNotificationSound(status)
        osNotificationReceivedEvent.complete(notification)*/
    }

    private fun showNotificationMessage(context: Context, title: String?, message: String?, intent: Intent?, type: String) {
        intent!!.putExtra("token", message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Oreo add channel
            if (notifManager == null) {
                notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val importance = NotificationManager.IMPORTANCE_HIGH
            if (mChannel == null) {
                mChannel = NotificationChannel("0", title, importance)
                /*val rawPathUri =
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.ring)
                *//*val r = RingtoneManager.getRingtone(context!!.applicationContext, rawPathUri)
                r.play()*//*
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                mChannel!!.setSound(rawPathUri, audioAttributes)*/
                mChannel!!.description = getString(context, R.string.app_name)
                mChannel!!.enableVibration(true)
                notifManager!!.createNotificationChannel(mChannel!!)
            }
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "0")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 1251, intent, PendingIntent.FLAG_IMMUTABLE)
            builder.setContentTitle(title)
                .setSmallIcon(R.drawable.med_connect_logo)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingIntent, true)
            val notification = builder.build()
            playNotificationSound(type, context)
            notifManager!!.notify(m, notification)
        } else {
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                context, m,
                intent, PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.med_connect_logo)
                .setContentTitle(title)
                .setSound(RingtoneManager.getDefaultUri(R.raw.message_tone))
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingIntent, true)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            playNotificationSound(type, context)
            notificationManager.notify(m, notificationBuilder.build())
        }
    }

    private fun playNotificationSound(type : String, context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        try {
            if (type.equals("call", true)) {
//                val playIntent = Intent(this, SoundService::class.java)
//                playIntent.action = SoundService.ACTION_PLAY
//                context.startService(playIntent)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(500) // Vibrate for 500 milliseconds
                }
                val rawPathUri =
                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.message_tone)
                val r = RingtoneManager.getRingtone(context!!.applicationContext, rawPathUri)
                r.play()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startSound(context: Context) {
        val playIntent = Intent(context, SoundService::class.java)
        playIntent.action = SoundService.ACTION_PLAY
        context.startService(playIntent)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent!!)
        } else {
            context.startService(intent)
        }*/
        /*Handler().postDelayed({
            //empSound("update")
        }, 30000)*/
    }

    private fun startService(context: Context, serviceClass: Class<*>, action: String, token: String?, doctorName: String?, doctorId: String?, playerId: String?, consultType : String?, bookingId: String?, familyMemberId : String?) {
        val intent = Intent(context, serviceClass).apply {
            putExtra("action", action)
            putExtra("token", token)
            putExtra("doctorName", doctorName)
            putExtra("doctorId", doctorId)
            putExtra("playerId", playerId)
            putExtra("consultType", consultType)
            putExtra("bookingId", bookingId)
            putExtra("familyMemberId", familyMemberId)
        }
        context.startService(intent)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }*/
    }

    private fun wakeUpScreen(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "MyApp::NotificationWakeLock"
        )
        wakeLock.acquire(3000) // Wake screen for 3 seconds
        wakeLock.release()
    }
}