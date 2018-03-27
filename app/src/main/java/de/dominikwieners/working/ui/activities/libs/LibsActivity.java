package de.dominikwieners.working.ui.activities.libs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.di.wkApplication;

public class LibsActivity extends AppCompatActivity {

    @BindView(R.id.libs_toolbar)
    Toolbar toolbar;

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libs);
        ButterKnife.bind(this);
        ((wkApplication) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LibsSupportFragment fragment = new LibsBuilder()
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutAppName(getString(R.string.app_name))
                .withExcludedLibraries("appcompat_v7", "recyclerview_v7", "support_v4", "support_annotations", "constraint_layout", "design", "support_cardview")
                .supportFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.libs_fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        navigator.showAboutActivity(this);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigator.showAboutActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
