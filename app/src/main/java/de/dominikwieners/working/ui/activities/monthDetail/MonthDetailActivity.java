package de.dominikwieners.working.ui.activities.monthDetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.Config;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.room.Work;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.monthDetail.MonthDetailPresenter;
import de.dominikwieners.working.ui.activities.monthDetail.adapter.MonthDetailAdapter;
import de.dominikwieners.working.ui.view.monthDetail.MonthDetailView;

public class MonthDetailActivity extends MvpActivity<MonthDetailView, MonthDetailPresenter> {

    @BindView(R.id.month_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.month_detail_recycler)
    RecyclerView recycler;

    @Inject
    Navigator navigator;

    int year;
    int month;

    String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);
        ButterKnife.bind(this);
        year = getIntent().getIntExtra(Config.SELECTED_YEAR, 0);
        month = getIntent().getIntExtra(Config.CURRENT_PAGER_POS, 0);
        months = getResources().getStringArray(R.array.months);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(String.format(getString(R.string.app_name_month_detail), months[month], year));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((wkApplication) getApplication()).getComponent().inject(this);
        List<String> list = getPresenter().loadWorkTypes(this, year, month);
        recycler.setAdapter(new MonthDetailAdapter(list, this, getPresenter(), month, year));
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recycler.hasFixedSize();

    }

    @NonNull
    @Override
    public MonthDetailPresenter createPresenter() {
        return new MonthDetailPresenter();
    }


    @Override
    public void onBackPressed() {
        navigator.showMainActivityWithPositionAndYear(this, month, year);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigator.showMainActivityWithPositionAndYear(this, month, year);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
