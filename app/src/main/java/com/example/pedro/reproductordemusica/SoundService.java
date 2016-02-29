package com.example.pedro.reproductordemusica;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

/**
 * Created by pedro on 25/02/16.
 */
public class SoundService extends Service{

    private NotificationCompat.Builder notification;
    private NotificationManager nm;
    private static final int id = 1;
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        mp = new MediaPlayer();

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        mp = MediaPlayer.create(this, Uri.parse(b.getString("music")));
        mp.start();
        makeNotification(b);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mp.stop();
        nm.cancel(id);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void makeNotification(Bundle b){

        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle("Reproduciendo musica");
        notification.setContentText(b.getString("name"));

        PendingIntent notificationAction = PendingIntent.getActivity(this, 0, new Intent(this, Main2Activity.class), 0);
        notification.setContentIntent(notificationAction);

        nm.notify(id, notification.build());
    }
}
