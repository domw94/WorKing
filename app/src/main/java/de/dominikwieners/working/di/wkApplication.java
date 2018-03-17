package de.dominikwieners.working.di;

import android.app.Application;


import de.dominikwieners.working.di.component.AppComponent;
import de.dominikwieners.working.di.component.DaggerAppComponent;
import de.dominikwieners.working.di.modul.AppModule;
import de.dominikwieners.working.di.modul.StorageModule;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class wkApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .storageModule(new StorageModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}
