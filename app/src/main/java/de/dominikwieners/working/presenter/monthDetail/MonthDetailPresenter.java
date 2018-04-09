package de.dominikwieners.working.presenter.monthDetail;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import de.dominikwieners.working.data.room.Work;
import de.dominikwieners.working.repository.WorkingDatabase;
import de.dominikwieners.working.ui.view.monthDetail.MonthDetailView;

/**
 * Created by dominikwieners on 01.04.18.
 */

public class MonthDetailPresenter extends MvpBasePresenter<MonthDetailView> {

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
     * Load all existing work types by month and year
     *
     * @param context
     * @param year
     * @param month
     * @return
     */
    public List<String> loadWorkTypes(Context context, int year, int month) {
        return WorkingDatabase
                .getInstance(context)
                .getWorkDao()
                .loadWorkingTypes(year, month);
    }

    /**
     * Get Sum of Min by type
     *
     * @param context
     * @param type
     * @param year
     * @param month
     * @return
     */
    public int getSumOfWorkingByType(Context context, String type, int year, int month) {
        int sum = 0;
        List<Work> list = loadWorkDataByMonth(context, year, month);
        for (Work work : list) {
            if (work.getWorkType().equals(type)) {
                sum += work.getTodaysMin();
            }
        }
        return sum;
    }

    public double getSumOfWorkingByTypeInHours(Context context, String type, int year, int month) {
        int min = getSumOfWorkingByType(context, type, year, month);
        return (double) min / 60;
    }
}
