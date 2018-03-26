package de.dominikwieners.working.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.repository.WorkingDatabase;
import de.dominikwieners.working.ui.activities.main.adapter.MainPagerAdapter;
import de.dominikwieners.working.ui.activities.main.fragments.MonthFragment;
import de.dominikwieners.working.ui.view.ActivityMainView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class ActivityMainPresenter extends MvpBasePresenter<ActivityMainView> {

    private int currentPagerPage;

    private int selectedYear;

    private int year;

    /**
     * Get all time relevant data
     */
    private void getCurrentDateTime() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        year = c.get(Calendar.YEAR);
    }


    /**
     * Get year (now)
     *
     * @return
     */
    public int getCurrentYear() {
        getCurrentDateTime();
        return year;
    }

    /**
     * Get Selected Year
     *
     * @return
     */
    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    /**
     * Loads work data from db
     *
     * @param context
     * @return
     */
    public List<Work> loadWorkDataByMonth(Context context, int year, int month) {
        return WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .loadDataByMonth(year, month);
    }

    /**
     * Load all available years from db
     * @param context
     * @return
     */
    public List<Integer> loadYearData(Context context) {
        return WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .loadYears();
    }

    /**
     * Delete working data by year
     *
     * @param year
     * @param context
     */
    public void deleteWorkByYear(int year, Context context) {
        WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .deleteByYear(year);
    }


    public void deleteWorkByMonth(int year, int month, Context context) {
        WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .deleteByMonth(year, month);
    }

    /**
     * Check If WelcomeActivity is done
     * @param sharedPreferences
     * @return
     */
    public int checkIfNextDone(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(Config.WELCOME_DONE, 0);
    }

    /**
     * Get month of current item
     * @return
     */
    public int getCurrentItemByMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Set pager to position x
     * @param currentPagerPage
     */
    public void setCurrentPagerPage(int currentPagerPage) {
        this.currentPagerPage = currentPagerPage;
    }

    /**
     * Get Position of selected pager page
     * @return
     */
    public int getCurrentPagerPosition() {
        return currentPagerPage;
    }


    /**
     * Get The Current month by extra
     *
     * @param intent
     * @return
     */
    public int getSelectedMonthByExtra(Intent intent) {
        if (intent.hasExtra(Config.CURRENT_PAGER_POS)) {
            return intent.getExtras().getInt(Config.CURRENT_PAGER_POS);
        } else if (intent.hasExtra(Config.SELECTED_YEAR)) {
            if (intent.getExtras().getInt(Config.SELECTED_YEAR) == getCurrentYear()) {
                return getCurrentItemByMonth();
            }
            return 0;
        }
        return getCurrentItemByMonth();
    }

    /**
     * Get Working time in minutes
     * @param selectedStartHour
     * @param selectedStartMin
     * @param selectedEndHour
     * @param selectedEndMin
     * @return
     */
    private int getTodaysMin(int selectedStartHour, int selectedStartMin, int selectedEndHour, int selectedEndMin) {
        int min;
        min = (selectedEndHour - selectedStartHour) * 60;
        min += (selectedEndMin - selectedStartMin);
        return min;
    }

    /**
     * Get Sum of working time of month
     * @param context
     * @param year
     * @param position
     * @return
     */
    private int sumOfMinutesOfMonth(Context context, int year, int position) {
        List<Work> list = loadWorkDataByMonth(context, year, position);
        int sum_of_min = 0;
        for (Work work : list) {
            sum_of_min += getTodaysMin(work.getStartHour(), work.getStartMin(), work.getEndHour(), work.getEndMin());
        }
        return sum_of_min;
    }

    /**
     * Get hour/minute format for position
     * @param context
     * @param year
     * @param position
     * @return
     */
    public String getSumOfHoursOfMonth(Context context, int year, int position) {
        int hours = sumOfMinutesOfMonth(context, year, position) / 60;
        int minutes = sumOfMinutesOfMonth(context, year, position) % 60;

        return String.format("%d.%02d h", hours, minutes);
    }

    /**
     * List all years in drawer which have got data from db
     * @param yearsList
     * @param menu
     * @param context
     */
    public void addYearMenuItemsToDrawer(List<Integer> yearsList, Menu menu, Context context) {
        Collections.reverse(yearsList);
        for (Integer item : yearsList) {
            menu.add(0, item.intValue(), 0, item.toString()).setIcon(R.mipmap.icon_bookmark);
        }
    }


}
