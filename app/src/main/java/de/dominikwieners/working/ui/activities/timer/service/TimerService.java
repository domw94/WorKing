package de.dominikwieners.working.ui.activities.timer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.dominikwieners.working.R;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.timer.TimerActivity;
import es.dmoral.toasty.Toasty;

/**
 * Created by dominikwieners on 20.03.18.
 */

public class TimerService extends Service {

    private static final String TAG = TimerService.class.getSimpleName();

    private Timer timer;
    // Start and end times in milliseconds
    private long lastTimestamp, secondsElapsed;

    //Time
    int seconds;
    int minutes;
    int hours;

    // Current Date
    int startYear;
    int startMonth;
    int startDay;
    int startHour;
    int startMinute;
    String startDayOfWeek;

    // Is the datetime already set?
    private boolean isSetCurrentDateTimer = false;

    // Is the service tracking time?
    private boolean isTimerRunning;

    // Foreground notification id
    private static final int NOTIFICATION_ID = 1;

    // Service binder
    private final IBinder serviceBinder = new RunServiceBinder();

    /**
     * Set all current time data
     */
    private void getCurrentDateTime() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        startDayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public class RunServiceBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Creating service");
        }
        isTimerRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting service");
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Binding service");
        }
        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Destroying service");
        }
    }

    /**
     * Starts the timer
     */
    public void startTimer() {
        if (!isTimerRunning) {
            timer = new Timer();
            if (!isSetCurrentDateTimer) {
                getCurrentDateTime();
            }
            isSetCurrentDateTimer = true;
            lastTimestamp = new Date().getTime() / 1000;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    long now = new Date().getTime() / 1000;
                    secondsElapsed += now - lastTimestamp;
                    lastTimestamp = now;
                }
            }, 0, 1000);
            isTimerRunning = true;
        } else {
            Log.e(TAG, "startTimer request for an already running timer");
        }
    }

    /**
     * Stops the timer
     */
    public void stopTimer() {
        if (isTimerRunning) {
            if (timer != null) {
                timer.cancel();
            }
            isTimerRunning = false;
        } else {
            Log.e(TAG, "stopTimer request for a timer that isn't running");
        }
    }

    /**
     * Resume the timer
     */
    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
        secondsElapsed = 0;
    }


    /**
     * @return whether the timer is running
     */
    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    /**
     * Returns the  elapsed time
     *
     * @return the elapsed time in seconds
     */
    public String elapsedTime() {
        // If the timer is running, the end time will be zero
        seconds = (int) secondsElapsed % 60;
        minutes = (int) secondsElapsed / 60;
        hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Get minutes from timer
     *
     * @return
     */
    public int getMinutes() {
        return minutes;
    }


    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public String getStartDayOfWeek() {
        return startDayOfWeek;
    }

    public int getEndMinute() {
        return (getStartMinute() + minutes) % 60;
    }

    public int getEndHour() {
        return (getStartHour() + hours) % 24;
    }
    /**
     * Place the service into the foreground
     */
    public void foreground() {
        startForeground(NOTIFICATION_ID, createNotification());
    }

    /**
     * Return the service to the background
     */
    public void background() {
        stopForeground(true);
    }

    /**
     * Creates a notification for placing the service into the foreground
     *
     * @return a notification for interacting with the service when in the foreground
     */
    private Notification createNotification() {

        NotificationManager manager;

        NotificationCompat.Builder mBuilder;

        mBuilder = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");

        Intent ii = new Intent(this.getApplicationContext(), TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        mBuilder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getResources().getString(R.string.timer_notification_head))
                .setContentText(getResources().getString(R.string.timer_notification_content))
                .setColor(getResources().getColor(R.color.colorTimerNotification))
                .setColorized(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification))
                .setContentIntent(pendingIntent);


        manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        return mBuilder.build();




/*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Timer Active")
                .setContentText("Tap to return to the timer")
                .setSmallIcon(R.mipmap.ic_launcher);

        Intent resultIntent = new Intent(this, TimerActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder.build();*/
    }
}


