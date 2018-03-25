package de.dominikwieners.working.ui.activities.working.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.dominikwieners.working.R;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import es.dmoral.toasty.Toasty;

/**
 * Created by dominikwieners on 20.03.18.
 */

public class NotificationService extends Service {

    //Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    //Random number Generator
    private final Random mGenerator = new Random();

    private final Timer mTimer = new Timer();

    private int time;

    private NotificationManager manager;

    NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {

        Toasty.info(this, getString(R.string.add_working_start_timer_service), Toast.LENGTH_LONG, false).show();
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    public void showNotification() {
        mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");

        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        mBuilder.setSmallIcon(R.drawable.ic_notification)
                .addAction(0, "STOP", pendingIntent)
                .addAction(0, "SAVE", pendingIntent)
                .setSound(null)
                .setContentTitle("Working Timer")
                .setContentText(Integer.toString(time))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon));

        manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, mBuilder.build());
    }

    public void timer() {
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time += 1;
                mBuilder.setContentText(Integer.toString(time));
                manager.notify(0, mBuilder.build());
            }
        }, 1000, 1000);
    }

}
