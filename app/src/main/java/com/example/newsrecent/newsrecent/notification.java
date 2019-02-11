package com.example.newsrecent.newsrecent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class notification extends Service {
    private static Long MILLISECS_PER_DAY = 86400000L;
    private String newsData;
    private String newsTitle;
    private static long delay = MILLISECS_PER_DAY;

    @Override
    public void onCreate() {
        super.onCreate();
        sendNotification();

    }
        public void setAlarm() {

            Intent serviceIntent = new Intent(this, notification.class);
            PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);

        }

        public void sendNotification() {

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            Type type = new TypeToken<List<Newsinfo>>(){}.getType();
            List<Newsinfo> NewsInfoList = gson.fromJson(json, type);
            if(NewsInfoList != null) {
                 newsData = NewsInfoList.get(0).getnDescription();
                 newsTitle = NewsInfoList.get(0).getnTitle();
            }
            else {
                newsTitle = "Top Headlines";
                newsData = "Open app to view top News";
            }
            Intent mainIntent = new Intent(this, MainActivity.class);
            @SuppressWarnings("deprecation")
            Notification noti = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle(newsTitle)
                    .setContentText(newsData)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_read_notification)
                    .setTicker(newsData)
                    .setWhen(System.currentTimeMillis())
                    .getNotification();

            NotificationManager notificationManager
                    = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(131315, noti);




        // Set an alarm for the next time this service should run:
        setAlarm();
        stopSelf();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
