package com.avs.sendme;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class SendMeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                createChannel(nm);
            }
        }
    }

    private static void createChannel(NotificationManager nm) {
        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = nm.getNotificationChannel(Values.CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(Values.CHANNEL_ID, Values.CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setSound(null, null);
                nm.createNotificationChannel(channel);
            }
        }
    }
}
