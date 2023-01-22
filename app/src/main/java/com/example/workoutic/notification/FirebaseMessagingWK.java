package com.example.workoutic.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.workoutic.R;
import com.example.workoutic.chat.MessageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;


public class FirebaseMessagingWK extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                }
                String token = task.getResult();
                updateToken(token);
            }
        });
    }
    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
            Token token = new Token(refreshToken);
            reference.child(firebaseUser.getUid()).setValue(token);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String sented = message.getData().get("sented");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && sented.equals(firebaseUser.getUid())){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendNotificationWK(message);

            }
            else{
                sendNotification(message);
            }
        }
    }
    private void sendNotificationWK(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");


        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationWK not = new NotificationWK(this);
        Notification.Builder builder = not.getNotificationWK(title, body, pendingIntent, icon);
        int i = 0;
        if (j > 0){
            i = j;
        }
        not.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_IMMUTABLE);

        RemoteViews  collapseView;
        RemoteViews expandedView;
            collapseView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
            expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
            collapseView.setTextViewText(R.id.text_view_collapsed_msg,body);
            expandedView.setTextViewText(R.id.text_view_expanded_msg,body);

        Bitmap iconLarge = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_workoutic_round);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(Integer.parseInt(icon))
                .setLargeIcon(iconLarge)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
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

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i = 0;
        if (j > 0){
            i = j;
        }
        noti.notify(i, builder.build());
    }


}
