package de.dominikwieners.working.ui.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.R;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.ActivityMainPresenter;
import de.dominikwieners.working.ui.activities.main.adapter.MainPagerAdapter;
import de.dominikwieners.working.ui.activities.main.fragments.MonthFragment;
import de.dominikwieners.working.ui.activities.working.service.NotificationService;
import de.dominikwieners.working.ui.view.ActivityMainView;
import io.reactivex.Observable;

public class MainActivity extends MvpActivity<ActivityMainView, ActivityMainPresenter> {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_drawer_navigation_view)
    NavigationView navigatorView;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_fab)
    FloatingActionButton fab;

    @BindView(R.id.main_hours)
    TextView tvhours;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Navigator navigator;

    private String[] months;

    private List<Work> works;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ((wkApplication) getApplication()).getComponent().inject(this);

        final MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());

        months = getResources().getStringArray(R.array.months);
        for (int i = 0; i < months.length; i++) {
            works = getPresenter().loadWorkDataByMonth(getApplicationContext(), i);
            adapter.addFragment(months[i], MonthFragment.newInstance(works));
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getPresenter().getSelectedMonthByExtra(getIntent()), true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    fab.hide();
                }
                tvhours.setText(getPresenter().getSumOfHoursOfMonth(getApplicationContext(), position));
                final int pos = position;
                ((MonthFragment) adapter.getItem(position)).getRecycler().addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {

                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {
                        tvhours.setText(getPresenter().getSumOfHoursOfMonth(getApplicationContext(), pos));
                    }
                });
                getPresenter().setCurrentPagerPage(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                fab.show();
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @NonNull
    @Override
    public ActivityMainPresenter createPresenter() {
        return new ActivityMainPresenter();
    }

    @OnClick(R.id.main_fab)
    public void onClickFab() {
        navigator.showAddWorkingActivityWithExtras(this);
    }

    @Override
    protected void onStart() {
        if (getPresenter().checkIfNextDone(sharedPreferences) == 0) {
            navigator.showWelcomeActivity(this);
        }
        super.onStart();
    }




}
