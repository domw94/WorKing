package de.dominikwieners.working.di;

import javax.inject.Singleton;

import dagger.Component;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

}
