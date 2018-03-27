package de.dominikwieners.working.ui.activities.about;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.Config;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.presenter.about.ActivityAboutPresnenter;
import de.dominikwieners.working.ui.view.about.ActivityAboutView;
import es.dmoral.toasty.Toasty;

public class AboutActivity extends MvpActivity<ActivityAboutView, ActivityAboutPresnenter> {

    @BindView(R.id.about_toolbar)
    Toolbar toolbar;

    @BindView(R.id.about_item_author_user_row)
    LinearLayout userItem;

    @BindView(R.id.about_item_author_mail_row)
    LinearLayout mailItem;

    @BindView(R.id.about_item_author_website_row)
    LinearLayout websiteItem;

    @BindView(R.id.about_item_about_version_row)
    LinearLayout versionItem;

    @BindView(R.id.about_item_tv_version_detail)
    TextView tvVersionDetail;

    @BindView(R.id.about_item_about_libs_row)
    LinearLayout libsItem;


    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.about_item));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((wkApplication) getApplication()).getComponent().inject(this);
        tvVersionDetail.setText(getPresenter().getVersionName());
    }

    @NonNull
    @Override
    public ActivityAboutPresnenter createPresenter() {
        return new ActivityAboutPresnenter();
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

    @OnClick(R.id.about_item_author_user_row)
    public void onClickUser() {
        navigator.showWebsite(this, Config.MY_GITHUB);
    }

    @OnClick(R.id.about_item_author_mail_row)
    public void onClickMail() {
        navigator.showMail(this, Config.MY_EMAIL_ADDRESS);
    }

    @OnClick(R.id.about_item_author_website_row)
    public void onClickWebsite() {
        navigator.showWebsite(this, Config.MY_WEBSITE);
    }

    @OnClick(R.id.about_item_about_libs_row)
    public void onClickLibs() {
        navigator.showLibsActivity(this);
    }
}
