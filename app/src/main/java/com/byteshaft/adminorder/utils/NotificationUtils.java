package com.byteshaft.adminorder.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.byteshaft.adminorder.AppGlobals;
import com.byteshaft.adminorder.R;


public class NotificationUtils {

    public NotificationUtils() {
    }

    public void showNotificationMessage(String title, String message, Intent intent) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
            // notification icon
            int icon = R.mipmap.ic_launcher;
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            AppGlobals.getContext(),
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    AppGlobals.getContext());
            Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(inboxStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(AppGlobals.getContext().getResources(), icon))
                    .build();

            NotificationManager notificationManager = (NotificationManager)
                    AppGlobals.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(AppGlobals.NOTIFICATION_ID, notification);
    }
}
