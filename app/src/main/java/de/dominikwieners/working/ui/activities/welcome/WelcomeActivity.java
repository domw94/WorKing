package de.dominikwieners.working.ui.activities.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.Toast;


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
import de.dominikwieners.working.presenter.ActivityWelcomePresenter;
import de.dominikwieners.working.ui.activities.welcome.adapter.TypeAdapter;
import de.dominikwieners.working.ui.view.ActivityWelcomeView;
import de.dominikwieners.working.di.wkApplication;
import es.dmoral.toasty.Toasty;

public class WelcomeActivity extends MvpActivity<ActivityWelcomeView, ActivityWelcomePresenter> implements ActivityWelcomeView {

    @BindView(R.id.welcome_toolbar)
    Toolbar toolbar;

    @BindView(R.id.welcome_box)
    ConstraintLayout welcomeView;

    @BindView(R.id.welcome_recycler)
    RecyclerView recycler;

    @BindView(R.id.welcome_fab)
    FloatingActionButton fab;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Navigator navigator;

    private int mState;

    List<Type> typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_welcome);
        ((wkApplication) getApplication()).getComponent().inject(this);
    }


    @NonNull
    @Override
    public ActivityWelcomePresenter createPresenter() {
        return new ActivityWelcomePresenter();
    }

    public Activity getActivity(Context context) {
        return (Activity) context;
    }

    @OnClick(R.id.welcome_fab)
    public void onClickFab() {
        showTypeDialog();
    }

    public void showTypeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.fragment_welcome_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(WelcomeActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText etType = (EditText) view.findViewById(R.id.dialog_et_type);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        getPresenter().goBackFromDialog(dialog, keyCode);
                        return true;
                    }
                })
                .setPositiveButton(getString(R.string.welcome_dialog_positiv), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = etType.getText().toString();
                        Type type = new Type(text);
                        presenter.insertTypeData(getApplicationContext(), type);
                        typeList = presenter.loadTypeData(getApplicationContext());
                        mState = Config.SHOW_MENU;
                        invalidateOptionsMenu();
                        recycler.setAdapter(new TypeAdapter(getPresenter(), getApplicationContext(), typeList));
                    }
                })
                .setNegativeButton(getString(R.string.welcome_dialog_negativ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        typeList = getPresenter().loadTypeData(this);

        if (typeList.isEmpty())
            mState = Config.HIDE_MENU;
        else {
            mState = Config.SHOW_MENU;
        }
        invalidateOptionsMenu();

        recycler.setAdapter(new TypeAdapter(getPresenter(), getApplicationContext(), typeList));
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.hasFixedSize();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_bottom);

        recycler.setLayoutAnimation(controller);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.scheduleLayoutAnimation();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.welcome_menu, menu);
        getPresenter().setStateOfNextItem(menu, mState);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.welcome_menu_next:

                SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.putInt(Config.WELCOME_DONE, 1);
                prefEditor.commit();

                navigator.showMainActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void showWelcome() {
        welcomeView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWelcome() {
        welcomeView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyFieldMessage() {
        Toasty.error(getApplicationContext(), getString(R.string.welcome_error_message_fields_empty), Toast.LENGTH_LONG, false).show();
    }

    @Override
    public void showIsMemeberMessage(Type memeber) {

        Toasty.error(getApplicationContext(), memeber.getType() + " is member of your types", Toast.LENGTH_LONG, false).show();
    }


}
