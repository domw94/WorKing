package de.dominikwieners.working.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.view.AddWorkingView;

/**
 * Created by dominikwieners on 17.03.18.
 */

public class AddWorkingPresenter extends MvpBasePresenter<AddWorkingView> {

    public String getCurentDate() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day + "." + (month + 1) + "." + year;
    }

    /**
     * Load Type data from Room db
     *
     * @param context
     * @return
     */
    public List<Type> loadData(Context context) {
        return TypeDatabase
                .getInstance(context)
                .getTypeDao()
                .getAll();
    }


    public String[] getTypeArray(Context context) {
        List<Type> typeList = loadData(context);
        String[] types = new String[typeList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            types[i] = typeList.get(i).getType();
        }
        return types;
    }
}
