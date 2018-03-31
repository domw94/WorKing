package de.dominikwieners.working.ui.activities.timer;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.Config;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.room.Work;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.timer.ActivityTimerPresenter;
import de.dominikwieners.working.ui.activities.timer.service.TimerService;
import de.dominikwieners.working.ui.view.timer.ActivityTimerView;
import es.dmoral.toasty.Toasty;

public class TimerActivity extends MvpActivity<ActivityTimerView, ActivityTimerPresenter> implements ActivityTimerView {


    @BindView(R.id.timer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.timer_time)
    TextView tvTimer;

    @BindView(R.id.timer_bu_reset)
    Button buReset;

    @BindView(R.id.timer_bu_start_stop_resume)
    Button buStartStop;

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
        ((wkApplication) getApplication()).getComponent().inject(this);
    }

    @NonNull
    @Override
    public ActivityTimerPresenter createPresenter() {
        return new ActivityTimerPresenter();
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onStart() {
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
        updateUIStart();
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning()) {
                timerService.foreground();
            } else if (timerService.isEndOfDay()) {
                // Do nothing if time not saved
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
                updateUISave();
            } else if (!timerService.isTimerRunning() && timerService.isEndOfDay()) {
                updateUIEndOfDay();
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
        if (serviceBound && !timerService.isTimerRunning() && timerService.isEndOfDay()) {
            String[] typeArray = getPresenter().getTypeArray(this);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TimerActivity.this);
            mBuilder.setSingleChoiceItems(typeArray, 0, null);
            mBuilder.setTitle(getResources().getString(R.string.timer_save_dialog_info_content));
            mBuilder.setPositiveButton(R.string.timer_save_dialog_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView lv = ((AlertDialog) dialog).getListView();
                    String workingType = lv.getAdapter().getItem(lv.getCheckedItemPosition()).toString();
                    Work work = new Work(workingType, timerService.getStartDayOfWeek(), timerService.getStartDay(), timerService.getStartMonth(), timerService.getStartYear(), timerService.getStartHour(), timerService.getStartMinute(), timerService.getEndHour(), timerService.getEndMinute(), timerService.getMinutes());
                    presenter.insertWorkData(getApplicationContext(), work);
                    navigator.showMainActivityWithPositionAndYear(getActivity(), timerService.getStartMonth(), timerService.getStartYear());
                    timerService.setEndOfDay(false);
                    Toasty.success(getApplicationContext(), getString(R.string.add_working_bu_save_succes_message), Toast.LENGTH_LONG, false).show();
                }
            });
            mBuilder.setNegativeButton(R.string.timer_save_dialog_cancle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            mBuilder.show();

        } else if (serviceBound && !timerService.isTimerRunning() && !timerService.isEndOfDay()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Starting timer");
            }
            timerService.startTimer();
            updateUISave();
        } else if (serviceBound && timerService.isTimerRunning() && !timerService.isEndOfDay()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Stopping timer");
            }
            if (timerService.getMinutes() < Config.TIMER_MINUTES_ON_ENABLE_SAVE) {
                Toasty.error(this, String.format(getString(R.string.timer_text_minute_error), Config.TIMER_MINUTES_ON_ENABLE_SAVE), Toast.LENGTH_LONG, false).show();
            } else {
                String[] typeArray = getPresenter().getTypeArray(this);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TimerActivity.this);
                mBuilder.setSingleChoiceItems(typeArray, 0, null);
                mBuilder.setTitle(getString(R.string.timer_save_dialog_info_content));
                mBuilder.setPositiveButton(R.string.timer_save_dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (serviceBound && timerService.isTimerRunning()) {
                            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                                Log.v(TAG, "Stopping timer");
                            }
                            timerService.stopTimer();
                        }
                        ListView lv = ((AlertDialog) dialog).getListView();
                        String workingType = lv.getAdapter().getItem(lv.getCheckedItemPosition()).toString();
                        Work work = new Work(workingType, timerService.getStartDayOfWeek(), timerService.getStartDay(), timerService.getStartMonth(), timerService.getStartYear(), timerService.getStartHour(), timerService.getStartMinute(), timerService.getEndHour(), timerService.getEndMinute(), timerService.getMinutes());
                        presenter.insertWorkData(getApplicationContext(), work);
                        navigator.showMainActivityWithPositionAndYear(getActivity(), timerService.getStartMonth(), timerService.getStartYear());
                        Toasty.success(getApplicationContext(), getString(R.string.add_working_bu_save_succes_message), Toast.LENGTH_LONG, false).show();
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
        tvTimer.setText(R.string.timer_text_placeholder);
        tvTimer.setBackground(getResources().getDrawable(R.drawable.circle_gray));
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
    private void updateUISave() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        buStartStop.setText(getString(R.string.timer_bu_save));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorTimerSaveGreen));
        }
        tvTimer.setBackground(getResources().getDrawable(R.drawable.circle_green));
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStart() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
        buStartStop.setText(getString(R.string.timer_bu_start));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        }
    }


    private void updateUIEndOfDay() {
        buStartStop.setText("End of day");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buStartStop.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorErrorRed));
        }
        tvTimer.setBackground(getResources().getDrawable(R.drawable.circle_red));
        tvTimer.setText(timerService.elapsedTime());
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
                    if (timerService.isEndOfDay()) {
                        updateUIEndOfDay();
                    }
                }
            });
        }

    }

}
