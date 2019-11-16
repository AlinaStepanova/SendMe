package com.avs.sendme.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.avs.sendme.Values;
import com.avs.sendme.main.MainActivity;
import com.avs.sendme.R;
import com.avs.sendme.provider.SendMeContract;
import com.avs.sendme.provider.SendMeProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.avs.sendme.Values.NOTIFICATION_ID;

public class SendMeFirebaseMessagingService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR = SendMeContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SendMeContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SendMeContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SendMeContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static String LOG_TAG = SendMeFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);
        // generated ones when app is first installed
        //dZ2mMylabvw:APA91bFbKluVaXr9hhBaCkiGtuDDxYbCVZpLm86bzJTnhWnhVHPTrIJyPe3nmCD2ywyJzqGfy7YNYC9IDXgYrfVUqxJyF7ubXbPS-mXmvTqEPeUi_9-r547JuK98kAK_5Jv7aFAqQjcE
        Log.d("Firebase", s);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            // Send a notification that you got a new message
            sendNotification(data);
            insertMessage(data);

        }
    }

    private void insertMessage(final Map<String, String> data) {

        // Database operations should not be done on the main thread
        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(SendMeContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                newMessage.put(SendMeContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                newMessage.put(SendMeContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                newMessage.put(SendMeContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                getContentResolver().insert(SendMeProvider.SquawkMessages.CONTENT_URI, newMessage);
                return null;
            }
        };

        insertSquawkTask.execute();
    }


    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */
    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message != null && message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Values.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_face_2)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            Log.d(LOG_TAG, "author: " + author);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}
