package de.dominikwieners.working.ui.activities.timer;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.ActivityTimerPresenter;
import de.dominikwieners.working.ui.activities.timer.service.TimerService;
import de.dominikwieners.working.ui.view.ActivityTimerView;

public class TimerActivity extends MvpActivity<ActivityTimerView, ActivityTimerPresenter> implements ActivityTimerView {


    @BindView(R.id.timer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.timer_time)
    TextView tvTimer;

    @BindView(R.id.timer_bu_reset)
    Button buReset;

    @BindView(R.id.timer_bu_start_stop_resume)
    Button buStartStop;

    @BindView(R.id.timer_bu_save)
    Button buSave;

    @Inject
    Navigator navigator;

    private static final String TAG = TimerActivity.class.getSimpleName();

    private TimerService timerService;
    private boolean serviceBound;

    // Handler to update the UI every second when the timer is running
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);

    // Message type for the handler
    private final static int MSG_UPDATE_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_timer));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((wkApplication) getApplication()).getComponent().inject(this);
    }

    @NonNull
    @Override
    public ActivityTimerPresenter createPresenter() {
        return new ActivityTimerPresenter();
    }


    @Override
    public void onBackPressed() {
        navigator.showMainActivity(this);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigator.showMainActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning()) {
                timerService.foreground();
            } else {
                stopService(new Intent(this, TimerService.class));
            }
            // Unbind the service
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    @OnClick(R.id.timer_bu_start_stop_resume)
    public void onStartTimerAndStop() {
        if (serviceBound && !timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Starting timer");
            }
            timerService.startTimer();
            buStartStop.setText("Stop");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPauseOrError));
            }
            updateUIStartRun();
        } else if (serviceBound && timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Stopping timer");
            }
            timerService.stopTimer();
            buStartStop.setText("Start");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
            }
            updateUIStopRun();
        }
    }

    @OnClick(R.id.timer_bu_reset)
    public void onReset() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Reset timer");
        }
        buStartStop.setText("Start");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }
        timerService.resetTimer();
    }

    @OnClick(R.id.timer_bu_save)
    public void onSave() {
        String[] typeArray = getPresenter().getTypeArray(this);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TimerActivity.this);
        mBuilder.setSingleChoiceItems(typeArray, 0, null);
        mBuilder.setTitle(R.string.timer_save_dialog_info);
        mBuilder.setPositiveButton(R.string.timer_save_dialog_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.setNegativeButton(R.string.timer_save_dialog_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.show();
    }

    static class UIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<TimerActivity> activity;

        UIUpdateHandler(TimerActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "updating time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        //timerButton.setText(R.string.timer_stop_button);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
        //timerButton.setText(R.string.timer_start_button);
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */
    private void updateUITimer() {
        if (serviceBound) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTimer.setText(timerService.elapsedTime());
                }
            });
        }
    }

}
