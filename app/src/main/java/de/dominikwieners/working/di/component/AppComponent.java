package de.dominikwieners.working.di.component;

import javax.inject.Singleton;

import dagger.Component;
import de.dominikwieners.working.di.modul.AppModule;
import de.dominikwieners.working.di.modul.StorageModule;
import de.dominikwieners.working.ui.activities.about.AboutActivity;
import de.dominikwieners.working.ui.activities.libs.LibsActivity;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.main.fragments.MonthFragment;
import de.dominikwieners.working.ui.activities.monthDetail.MonthDetailActivity;
import de.dominikwieners.working.ui.activities.settings.SettingsActivity;
import de.dominikwieners.working.ui.activities.timer.TimerActivity;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;
import de.dominikwieners.working.ui.activities.working.AddWorkingActivity;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Singleton
@Component(modules = {AppModule.class, StorageModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(WelcomeActivity welcomeActivity);
    void inject(AddWorkingActivity addWorkingActivity);
    void inject(AboutActivity aboutActivity);

    void inject(MonthDetailActivity monthDetailActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(LibsActivity libsActivity);
    void inject(TimerActivity timerActivity);
    void inject(MonthFragment monthFragment);
}
