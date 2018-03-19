package de.dominikwieners.working.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.repository.WorkingDatabase;
import de.dominikwieners.working.ui.view.ActivityMainView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class ActivityMainPresenter extends MvpBasePresenter<ActivityMainView> {

    private int currentPagerPage;

    /**
     * Loads work data from db
     *
     * @param context
     * @return
     */
    public List<Work> loadWorkDataByMonth(Context context, int month) {
        return WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .loadDataByMonth(month);
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
        }
        return getCurrentItemByMonth();
    }

    private int getTodaysMin(int selectedStartHour, int selectedStartMin, int selectedEndHour, int selectedEndMin) {
        int min;
        min = (selectedEndHour - selectedStartHour) * 60;
        min += (selectedEndMin - selectedStartMin);
        return min;
    }

    private int sumOfMinutesOfMonth(Context context, int position) {
        List<Work> list = loadWorkDataByMonth(context, position);
        int sum_of_min = 0;
        for (Work work : list) {
            sum_of_min += getTodaysMin(work.getStartHour(), work.getStartMin(), work.getEndHour(), work.getEndMin());
        }
        return sum_of_min;
    }

    public String getSumOfHoursOfMonth(Context context, int position) {
        int hours = sumOfMinutesOfMonth(context, position) / 60;
        int minutes = sumOfMinutesOfMonth(context, position) % 60;

        return String.format("%d.%02d h", hours, minutes);
    }


}
