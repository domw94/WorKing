package de.dominikwieners.working.presenter;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import de.dominikwieners.working.ui.view.FragmentWorkView;

/**
 * Created by dominikwieners on 18.03.18.
 */

public class FragmentWorkPresenter extends MvpBasePresenter<FragmentWorkView> {

    /**
     * Get DateFormat with Day
     *
     * @param dayOfWeek
     * @param day
     * @param month
     * @param year
     * @return
     */
    public String getDateFromatWidthDay(String dayOfWeek, int day, int month, int year) {
        return String.format("%s %02d.%02d.%04d", dayOfWeek, day, month + 1, year);
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
}
