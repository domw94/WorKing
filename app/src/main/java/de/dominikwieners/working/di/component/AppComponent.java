package de.dominikwieners.working.di.component;

import javax.inject.Singleton;

import dagger.Component;
import de.dominikwieners.working.di.modul.AppModule;
import de.dominikwieners.working.di.modul.StorageModule;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Singleton
@Component(modules = {AppModule.class, StorageModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(WelcomeActivity welcomeActivity);
}
