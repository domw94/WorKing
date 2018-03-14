package de.dominikwieners.working.ui.activities.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.presenter.WelcomePresenter;
import de.dominikwieners.working.ui.activities.welcome.adapter.TypeAdapter;
import de.dominikwieners.working.ui.view.WelcomeView;
import es.dmoral.toasty.Toasty;

public class WelcomeActivity extends MvpActivity<WelcomeView, WelcomePresenter> implements WelcomeView {

    @BindView(R.id.welcome_toolbar)
    Toolbar toolbar;

    @BindView(R.id.welcome_box)
    ConstraintLayout welcomeView;

    @BindView(R.id.welcome_recycler)
    RecyclerView recycler;

    @BindView(R.id.welcome_fab)
    FloatingActionButton fab;

    List<Type> typeList;

    private boolean donext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_welcome);
    }


    @NonNull
    @Override
    public WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }

    public Activity getActivity(Context context) {
        return (Activity) context;
    }

    @OnClick(R.id.welcome_fab)
    public void clickFab() {
        showTypeDialog();
    }

    public void showTypeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.type_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(WelcomeActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText etType = (EditText) view.findViewById(R.id.dialog_et_type);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return true;
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = etType.getText().toString();
                        Type type = new Type(presenter.getRandomMaterialColor("400", getApplicationContext()), text);
                        typeList.add(type);
                        getPresenter().insert(getApplicationContext(), type);
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
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
        typeList = getPresenter().loadData(this);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.welcome_menu_next:
                Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.welcome_menu_next);
        item.setEnabled(donext);
        return true;
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

    @Override
    public void showNextButton() {

    }

    @Override
    public void hideNextButton() {

    }


}
