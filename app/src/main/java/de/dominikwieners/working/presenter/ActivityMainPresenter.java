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
    public List<Work> loadWorkDataByMonth(Context context, int position) {
        return WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .loadDataByMonth(position);
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

}
