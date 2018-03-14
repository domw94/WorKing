package de.dominikwieners.working.ui.activities.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.R;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.MainPresenter;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;
import de.dominikwieners.working.ui.view.MainView;

public class MainActivity extends MvpActivity<MainView, MainPresenter> {

    @Inject
    TypeDatabase db;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    protected void onStart() {
        //TODO replayce with SharedPreferences when next has been clicked
        //if(getPresenter().loadData(db).isEmpty()) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
        //}
        super.onStart();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }
}
