package de.dominikwieners.working.ui.activities.working;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.Config;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.AddWorkingPresenter;
import de.dominikwieners.working.ui.activities.working.fragments.DatePickerFragment;
import de.dominikwieners.working.ui.view.AddWorkingView;

public class AddWorkingActivity extends MvpActivity<AddWorkingView, AddWorkingPresenter> implements AddWorkingView, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.add_working_toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_working_tv_month)
    TextView tvMonth;

    @BindView(R.id.add_working_et_date)
    EditText etDate;

    @BindView(R.id.add_working_sp_type)
    Spinner spType;

    @Inject
    Navigator navigator;

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
        String month = getIntent().getExtras().getString(Config.CURRENT_MONTH);
        pagerPos = getIntent().getExtras().getInt(Config.CURRENT_PAGER_POS);
        tvMonth.setText(month);

        etDate.setText(presenter.getCurentDate());
        etDate.setFocusable(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, presenter.getTypeArray(getApplicationContext()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
    }

    @NonNull
    @Override
    public AddWorkingPresenter createPresenter() {
        return new AddWorkingPresenter();
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
        etDate.setText(dayOfMonth + "." + (month + 1) + "." + year);
        tvMonth.setText(months[month]);
        pagerPos = month;
    }
}
