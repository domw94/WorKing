package de.dominikwieners.working.ui.activities.working;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.ActivityAddWorkingPresenter;
import de.dominikwieners.working.ui.activities.working.fragments.DatePickerFragment;
import de.dominikwieners.working.ui.view.ActivityAddWorkingView;
import es.dmoral.toasty.Toasty;

public class AddWorkingActivity extends MvpActivity<ActivityAddWorkingView, ActivityAddWorkingPresenter> implements ActivityAddWorkingView, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.add_working_toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_working_tv_month)
    TextView tvMonth;

    @BindView(R.id.add_working_et_date)
    EditText etDate;

    @BindView(R.id.add_working_sp_type)
    Spinner spType;

    @BindView(R.id.add_working_et_from)
    EditText etFrom;

    @BindView(R.id.add_working_et_until)
    EditText etUntil;

    @BindView(R.id.add_working_bu_start_timer)
    Button timer;

    @BindView(R.id.add_working_bu_save)
    Button buSave;

    @Inject
    Navigator navigator;

    private String selectedDayOfWeek;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    private int selectedStartHour;
    private int selectedStartMin;

    private int selectedEndHour;
    private int selectedEndMin;

    private int selectedTodaysMin;

    private String[] months;

    private int pagerPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_working);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_add_working));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((wkApplication) getApplication()).getComponent().inject(this);
        months = getResources().getStringArray(R.array.months);

        tvMonth.setText(months[presenter.getCurrentMonth()]);

        pagerPos = presenter.getCurrentMonth();

        selectedDayOfWeek = presenter.getGetDayOfWeek();
        selectedDay = presenter.getCurrentDay();
        selectedMonth = presenter.getCurrentMonth();
        selectedYear = presenter.getCurentYear();

        etDate.setText(presenter.getCalendarFormat(selectedDay, selectedMonth, selectedYear));
        etDate.setFocusable(false);
        etDate.setFocusableInTouchMode(false);

        etFrom.setFocusable(false);
        etFrom.setFocusableInTouchMode(false);

        etUntil.setFocusable(false);
        etUntil.setFocusableInTouchMode(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, getPresenter().getTypeArray(getApplicationContext()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
    }

    @NonNull
    @Override
    public ActivityAddWorkingPresenter createPresenter() {
        return new ActivityAddWorkingPresenter();
    }

    @Override
    public void onBackPressed() {
        navigator.showMainActivityWithPosition(this, pagerPos);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigator.showMainActivityWithPosition(this, pagerPos);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_working_et_date)
    public void onClickEtDate() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), datePicker.getTag());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etDate.setText(presenter.getCalendarFormat(dayOfMonth, month, year));
        tvMonth.setText(months[month]);
        selectedDay = dayOfMonth;
        selectedMonth = month;
        selectedYear = year;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.GERMAN);
        Date date = new Date(selectedYear, selectedMonth, selectedDay - 1);
        selectedDayOfWeek = simpleDateFormat.format(date);

        pagerPos = month;
    }


    @OnClick(R.id.add_working_et_from)
    public void onClickFrom() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedStartHour = hourOfDay;
                selectedStartMin = minute;
                etFrom.setText(presenter.getTimeFormat(selectedStartHour, selectedStartMin));
            }
        }, h, m, true);
        timePickerDialog.show();
    }

    @OnClick(R.id.add_working_et_until)
    public void onClickUntil() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int h = 18;
        int m = 0;
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedEndHour = hourOfDay;
                selectedEndMin = minute;
                etUntil.setText(presenter.getTimeFormat(selectedEndHour, selectedEndMin));
            }
        }, h, m, true);
        timePickerDialog.show();
    }


    @OnClick(R.id.add_working_bu_start_timer)
    public void onClickTimer() {
        /*  if (mBound) {
            mService.showNotification();
            mService.timer();
        }*/
        navigator.showTimerActivity(this);
    }

    @OnClick(R.id.add_working_bu_save)
    public void onClickSave() {
        String workingType = spType.getItemAtPosition(spType.getSelectedItemPosition()).toString();
        selectedTodaysMin = presenter.getTodaysMin(selectedStartHour, selectedStartMin, selectedEndHour, selectedEndMin);
        if (selectedTodaysMin > 0) {
            Work work = new Work(workingType, selectedDayOfWeek, selectedDay, selectedMonth, selectedYear, selectedStartHour, selectedStartMin, selectedEndHour, selectedEndMin, 1);
            presenter.insertWorkData(this, work);
            navigator.showMainActivityWithPositionAndYear(this, pagerPos, selectedYear);
            Toasty.success(getApplicationContext(), getString(R.string.add_working_bu_save_succes_message), Toast.LENGTH_LONG, false).show();
        } else {
            Toasty.error(getApplicationContext(), getString(R.string.error_times_incorrect), Toast.LENGTH_LONG, false).show();
        }
    }



}
