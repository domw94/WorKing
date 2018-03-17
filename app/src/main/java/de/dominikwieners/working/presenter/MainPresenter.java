package de.dominikwieners.working.presenter;

import android.content.SharedPreferences;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.view.MainView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private int currentPagerPage;

    public List<Type> loadData(TypeDatabase db) {
        return db.getTypeDao().getAll();
    }

    public int checkIfNextDone(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(Config.WELCOME_DONE, 0);
    }

    public int getCurrentItemByMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.MONTH);
    }

    public void setCurrentPagerPage(int currentPagerPage) {
        this.currentPagerPage = currentPagerPage;
    }

    public int getCurrentPagerPosition() {
        return currentPagerPage;
    }
}
