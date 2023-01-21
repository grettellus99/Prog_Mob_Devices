package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


//import com.example.workoutic.activity.UserActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import com.example.workoutic.exercises.Exercises;
import com.example.workoutic.routines.Routine_Main;
import com.example.workoutic.signin_signup.LoginActivity;
import com.example.workoutic.signin_signup.RegisterActivity;



public class MainActivity extends AppCompatActivity {
    private View exercisesView;
    private View routineView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Calendar calendar = Calendar.getInstance();

        Intent intent = new Intent(MainActivity.this, TipNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 300, pendingIntent);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);


    }

    private void setAlarm(){

    }

    public void goExercises(View view) {
        Intent intExercises = new Intent(getApplicationContext(), Exercises.class);
        startActivity(intExercises);
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","MainActivity");
        startActivity(intMenu);
    }

    public void goRegister(View view) {
        Intent intUser = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intUser);
    }

    public void goRoutines(View view) {
        Intent intRoutines= new Intent(getApplicationContext(), Routine_Main.class);
        startActivity(intRoutines);
    }

    public void goChat(View view) {
        Intent intChat= new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intChat);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "TipNotificationChannel";
            String description = "Notification for a tip";
            NotificationChannel notificationChannel = new NotificationChannel("tipNotification", name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}