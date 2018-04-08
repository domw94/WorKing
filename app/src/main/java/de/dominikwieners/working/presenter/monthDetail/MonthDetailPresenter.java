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
}
