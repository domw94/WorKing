package de.dominikwieners.working;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;
import de.dominikwieners.working.ui.activities.working.AddWorkingActivity;

/**
 * Created by dominikwieners on 17.03.18.
 */

@Singleton
public class Navigator {

    @Inject
    Navigator() {
    }

    ////////////////////////////////////////////////////
    // WelcomeActivity
    ///////////////////////////////////////////////////

    public void showWelcomeActivity(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    ////////////////////////////////////////////////////
    // MainActivity
    ///////////////////////////////////////////////////

    public void showMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void showMainActivityWithPosition(Activity activity, int pagerPos) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Config.CURRENT_PAGER_POS, pagerPos);
        activity.startActivity(intent);
        activity.finish();
    }

    ////////////////////////////////////////////////////
    // AddWorkingActivity
    ///////////////////////////////////////////////////

    public void showAddWorkingActivity(Activity activity) {
        Intent intent = new Intent(activity, AddWorkingActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void showAddWorkingActivityWithExtras(Activity activity) {
        Intent intent = new Intent(activity, AddWorkingActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }





}
