package lourdes8122.radiotaller.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import lourdes8122.radiotaller.MainActivity;
import lourdes8122.radiotaller.R;

public class AlarmReceiver extends BroadcastReceiver {

        private static final int NOTIFICATION_ID = 0;

        public AlarmReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Create the content intent for the notification, which launches this activity
            Intent contentIntent = new Intent(context, MainActivity.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity
                    (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logoradio);

            //Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_radio_black_24dp)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(context.getString(R.string.alarm_notification_title))
                    .setContentText(context.getString(R.string.alarm_notification_text))
                    .setContentIntent(contentPendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            //Deliver the notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
}
