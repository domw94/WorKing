package de.dominikwieners.working.ui.activities.working;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.Config;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.di.wkApplication;

public class AddWorkingActivity extends AppCompatActivity {

    @BindView(R.id.add_working_toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_working_tv_month)
    TextView tvMonth;

    @Inject
    Navigator navigator;

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

        String month = getIntent().getExtras().getString(Config.CURRENT_MONTH);
        pagerPos = getIntent().getExtras().getInt(Config.CURRENT_PAGER_POS);
        tvMonth.setText(month);
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
}
