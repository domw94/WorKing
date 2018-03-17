package de.dominikwieners.working.ui.activities.main;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.R;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.MainPresenter;
import de.dominikwieners.working.ui.activities.main.adapter.MainPagerAdapter;
import de.dominikwieners.working.ui.activities.main.fragments.MonthFragment;
import de.dominikwieners.working.ui.view.MainView;

public class MainActivity extends MvpActivity<MainView, MainPresenter> {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.main_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_fab)
    FloatingActionButton fab;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Navigator navigator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        ((wkApplication) getApplication()).getComponent().inject(this);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment("January", new MonthFragment());
        adapter.addFragment("February", new MonthFragment());
        adapter.addFragment("March", new MonthFragment());
        adapter.addFragment("April", new MonthFragment());
        adapter.addFragment("May", new MonthFragment());
        adapter.addFragment("June", new MonthFragment());
        adapter.addFragment("July", new MonthFragment());
        adapter.addFragment("August", new MonthFragment());
        adapter.addFragment("September", new MonthFragment());
        adapter.addFragment("October", new MonthFragment());
        adapter.addFragment("November", new MonthFragment());
        adapter.addFragment("December", new MonthFragment());


        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getPresenter().getCurrentItemByMonth(), true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    fab.hide();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("ScrollStateCh s", Integer.toString(state));
                fab.show();
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onStart() {
        if (getPresenter().checkIfNextDone(sharedPreferences) == 0) {
            navigator.showWelcomeActivity(this);
        }
        super.onStart();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }


}
