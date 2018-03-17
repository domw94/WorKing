package de.dominikwieners.working;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;

/**
 * Created by dominikwieners on 17.03.18.
 */

@Singleton
public class Navigator {

    @Inject
    Navigator() {
    }

    public void showWelcomeActivity(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void showMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
