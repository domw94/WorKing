package de.dominikwieners.working.ui.activities.timer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.ui.activities.timer.TimerActivity;

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

    private boolean isEndOfDay;

    public boolean isEndOfDay() {
        return isEndOfDay;
    }

    public void setEndOfDay(boolean endOfDay) {
        isEndOfDay = endOfDay;
    }

    // Foreground notification id
    private static final int NOTIFICATION_ID = 1;

    // Service binder
    private final IBinder serviceBinder = new RunServiceBinder();

    /**
     * Set all current time data
     */
    private void setStartDateTime() {
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
                setStartDateTime();
            }
            isSetCurrentDateTimer = true;
            lastTimestamp = new Date().getTime() / 1000;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long now = new Date().getTime() / 1000;
                    secondsElapsed += now - lastTimestamp;
                    lastTimestamp = now;
                    System.out.println(String.format("%d:%d:%d", getCurrentHour(), getCurrentMinute(), getCurrentSecond()));
                    if (getCurrentHour() == Config.TIMER_DAY_LIMIT_HOURS &&
                            getCurrentMinute() == Config.TIMER_DAY_LIMIT_MINUTES &&
                            getCurrentSecond() == Config.TIMER_DAY_LIMIT_SECONDS && !isEndOfDay()) {
                        setEndOfDay(true);
                        stopTimer();
                        foreground();
                    }
                }
            }, 1000, 1000);
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
        setEndOfDay(false);
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
        return String.format("%02d:%02d:%02d", hours, (minutes % 60), seconds);
    }

    /**
     * Get minutes from timer
     *
     * @return
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Get hour from timer
     *
     * @return
     */
    public int getHours() {
        return hours;
    }

    public int getCurrentSecond() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        return c.get(Calendar.SECOND);
    }

    public int getCurrentMinute() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        return c.get(Calendar.MINUTE);
    }

    public int getCurrentHour() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        return c.get(Calendar.HOUR_OF_DAY);
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
        if (!isEndOfDay()) {
            startForeground(NOTIFICATION_ID, createNotification());
        } else {
            startForeground(NOTIFICATION_ID, createDayEndNotification());
        }
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

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // The id of the channel.
        String id = "wk_channel_01";

        // The user-visible name of the channel.
        CharSequence name = "Notification in Background";

        // The user-visible description of the channel.
        String description = "Shows Notification in Background for running Timer";

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent ii = new Intent(this.getApplicationContext(), TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.Builder mBuilderTimer;
        mBuilderTimer = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getResources().getString(R.string.timer_notification_head))
                .setContentText(getResources().getString(R.string.timer_notification_content))
                .setColor(getResources().getColor(R.color.colorTimerNotification))
                .setColorized(true)
                .setContentIntent(pendingIntent);

        return mBuilderTimer.build();


    }

    private Notification createDayEndNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // The id of the channel.
        String id = "wk_channel_02";

        // The user-visible name of the channel.
        CharSequence name = "Day Notification";

        // The user-visible description of the channel.
        String description = "Shows Notification for ending of days";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);

            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(R.color.colorErrorRed);
            mChannel.enableVibration(true);

            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent ii = new Intent(this.getApplicationContext(), TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getResources().getString(R.string.timer_notification_day_head))
                .setContentText(getResources().getString(R.string.timer_notification_day_content))
                .setColor(getResources().getColor(R.color.colorErrorRed))
                .setColorized(true)
                .setContentIntent(pendingIntent);

        return mBuilder.build();

    }

}


