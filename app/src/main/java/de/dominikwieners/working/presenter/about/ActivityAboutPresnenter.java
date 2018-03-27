package de.dominikwieners.working.presenter.about;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import de.dominikwieners.working.BuildConfig;
import de.dominikwieners.working.ui.view.about.ActivityAboutView;

/**
 * Created by dominikwieners on 27.03.18.
 */

public class ActivityAboutPresnenter extends MvpBasePresenter<ActivityAboutView> {

    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
