package com.example.workoutic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TipNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "tipNotification").setSmallIcon(R.drawable.ic_user_account_45).setContentTitle("TITULO").setContentText("HOLA").setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(10, builder.build());



    }
}
