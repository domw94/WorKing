package de.dominikwieners.working.presenter;

import android.app.ActivityManager;
import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.repository.WorkingDatabase;
import de.dominikwieners.working.ui.view.ActivityAddWorkingView;

/**
 * Created by dominikwieners on 17.03.18.
 */

public class ActivityAddWorkingPresenter extends MvpBasePresenter<ActivityAddWorkingView> {

    private String getDayOfWeek;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    /**
     * Set all current time data
     */
    private void getCurrentDateTime() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        getDayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }


    /**
     * Get day of week as String (now)
     *
     * @return
     */
    public String getGetDayOfWeek() {
        return getDayOfWeek;
    }

    /**
     * Get hour of day (now)
     *
     * @return
     */
    public int getHour() {
        getCurrentDateTime();
        return hour;
    }

    /**
     * Get minute of day (now)
     *
     * @return
     */
    public int getMinute() {
        getCurrentDateTime();
        return minute;
    }

    /**
     * Get day (now)
     *
     * @return
     */
    public int getCurrentDay() {
        getCurrentDateTime();
        return day;
    }

    /**
     * Get month (now)
     *
     * @return
     */
    public int getCurrentMonth() {
        getCurrentDateTime();
        return month;
    }

    /**
     * Get year (now)
     *
     * @return
     */
    public int getCurentYear() {
        getCurrentDateTime();
        return year;
    }

    /**
     * Get Format by DD.MM.YYYY
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public String getCalendarFormat(int day, int month, int year) {
        return String.format("%02d.%02d.%04d", day, month + 1, year);
    }


    /**
     * Load Type data from Room db
     *
     * @param context
     * @return
     */
    public List<Type> loadTypeData(Context context) {
        return WorkingDatabase
                .getInstance(context)
                .getTypeDao()
                .getAll();
    }

    /**
     * Get String array of all types
     *
     * @param context
     * @return
     */
    public String[] getTypeArray(Context context) {
        List<Type> typeList = loadTypeData(context);
        String[] types = new String[typeList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            types[i] = typeList.get(i).getType();
        }
        return types;
    }

    /**
     * Inset Work into db
     *
     * @param context
     * @param work
     */
    public void insertWorkData(Context context, Work work) {
        WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .insertAll(work);
    }

    /**
     * Get Time Format
     *
     * @param hourOfDay
     * @param minute
     * @return
     */
    public String getTimeFormat(int hourOfDay, int minute) {
        return String.format("%02d:%02d Uhr", hourOfDay, minute);
    }

    /**
     * Sum todays minutes
     *
     * @param selectedStartHour
     * @param selectedStartMin
     * @param selectedEndHour
     * @param selectedEndMin
     * @return
     */
    public int getTodaysMin(int selectedStartHour, int selectedStartMin, int selectedEndHour, int selectedEndMin) {
        int min;
        min = (selectedEndHour - selectedStartHour) * 60;
        min += (selectedEndMin - selectedStartMin);
        return min;
    }


    public boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
