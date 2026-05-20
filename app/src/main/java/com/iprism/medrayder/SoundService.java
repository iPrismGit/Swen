package com.iprism.medrayder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class SoundService extends Service {
    private Ringtone ringtone;
    private Vibrator vibrator;

    public static final String ACTION_PLAY = "com.example.ACTION_PLAY";
    public static final String ACTION_STOP = "com.example.ACTION_STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            switch (intent.getAction()) {
                case ACTION_PLAY:
                    playRingtone();
                    startVibration();
                    break;
                case ACTION_STOP:
                    stopRingtone();
                    stopVibration();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void playRingtone() {
        if (ringtone == null) {
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        }
        if (ringtone != null && !ringtone.isPlaying()) {
            ringtone.play();
        }
    }

    private void stopRingtone() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }

    private void startVibration() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            long[] pattern = {0, 1000, 1000}; // Wait 0ms, vibrate 1000ms, wait 1000ms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0)); // Loop
            } else {
                vibrator.vibrate(pattern, 0); // Loop
            }
        }
    }

    private void stopVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRingtone();
        stopVibration();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}