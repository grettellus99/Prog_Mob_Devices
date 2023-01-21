package com.example.workoutic.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.workoutic.R;
import com.example.workoutic.util.NetUtil;

import java.util.Calendar;


public class NotificationWK extends ContextWrapper {
    private static final String CHANNEL_ID = "com.example.workoutic";
    private static final String CHANNEL_NAME = "chat";


    private NotificationManager notificationManager;
    public NotificationWK(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return  notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public  Notification.Builder getNotificationWK(String title, String body,
                                                   PendingIntent pendingIntent, String icon){
        RemoteViews  collapseView;
        RemoteViews expandedView;
        collapseView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        collapseView.setTextViewText(R.id.text_view_collapsed_msg,body);
        expandedView.setTextViewText(R.id.text_view_expanded_msg,body);

        collapseView.setTextViewText(R.id.text_view_collapsed_username,title);
        expandedView.setTextViewText(R.id.text_view_expanded_username,title);

        Bitmap iconLarge = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_workoutic_round);

        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(Integer.parseInt(icon))
                .setLargeIcon(iconLarge)
                .setStyle(new Notification.DecoratedCustomViewStyle())
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setShowWhen(true)
                .setCustomContentView(collapseView)
                .setCustomBigContentView(expandedView)
                .setDefaults(Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE)
                .setSound(
                        RingtoneManager.getDefaultUri(
                                RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(getResources().getColor(R.color.brand_c2), 0, 1)
                .setAutoCancel(true);
    }
}
