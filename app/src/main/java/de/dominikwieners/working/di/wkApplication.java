package de.dominikwieners.working.di;

import android.app.Application;

import dagger.internal.DaggerCollections;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class wkApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}
