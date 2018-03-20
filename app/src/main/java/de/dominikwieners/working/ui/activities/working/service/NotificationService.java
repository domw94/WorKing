package de.dominikwieners.working.ui.activities.working.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import de.dominikwieners.working.R;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import es.dmoral.toasty.Toasty;

/**
 * Created by dominikwieners on 20.03.18.
 */

public class NotificationService extends Service {

    private NotificationManager manager;


    @Override
    public void onCreate() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_timer);

        contentView.setOnClickPendingIntent(R.id.notification_button, getStopPendingIntent());

        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setCustomBigContentView(contentView);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, mBuilder.build());
        Toasty.info(this, getString(R.string.add_working_start_timer_service), Toast.LENGTH_LONG, false).show();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        manager.cancel(0);
        Toast.makeText(this, "STOP TIMER", Toast.LENGTH_LONG).show();

    }

    protected PendingIntent getStopPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        return pendingIntent;
    }

}
