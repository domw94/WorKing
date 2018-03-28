package de.dominikwieners.working.ui.activities.settings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.local.SettingsType;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.settings.ActivitySettingsPresenter;
import de.dominikwieners.working.ui.activities.settings.adapter.SettingsAdapter;
import de.dominikwieners.working.ui.view.settings.ActivitySettingsView;

public class SettingsActivity extends MvpActivity<ActivitySettingsView, ActivitySettingsPresenter> {

    @BindView(R.id.settings_toolbar)
    Toolbar toolbar;

    @BindView(R.id.settings_recycler)
    RecyclerView recycler;

    @Inject
    Navigator navigator;

    List<SettingsType> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((wkApplication) getApplication()).getComponent().inject(this);

        list = new ArrayList<>();
        list.add(new SettingsType(1, R.mipmap.icon_edit, getString(R.string.settings_edit_types)));
        recycler.setAdapter(new SettingsAdapter(list, this, navigator));
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recycler.hasFixedSize();
    }

    @NonNull
    @Override
    public ActivitySettingsPresenter createPresenter() {
        return new ActivitySettingsPresenter();
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

}
