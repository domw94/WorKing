package de.dominikwieners.working.ui.activities.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.presenter.WelcomePresenter;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.activities.main.MainActivity;
import de.dominikwieners.working.ui.activities.welcome.adapter.TypeAdapter;
import de.dominikwieners.working.di.wkApplication;
import de.dominikwieners.working.ui.view.WelcomeView;
import es.dmoral.toasty.Toasty;

public class WelcomeActivity extends MvpActivity<WelcomeView, WelcomePresenter> implements WelcomeView {

    @BindView(R.id.welcome_box)
    ConstraintLayout welcomeView;

    @BindView(R.id.welcome_recycler)
    RecyclerView recycler;

    @BindView(R.id.welcome_texbar_et_add)
    EditText etAdd;

    @BindView(R.id.welcome_textbar_bu_add)
    ImageButton buAdd;

    @BindView(R.id.welcome_bu_next)
    Button buNext;

    @Inject
    TypeDatabase db;

    List<Type> typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        ((wkApplication) getApplication()).getComponent().inject(this);
    }


    @NonNull
    @Override
    public WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }


    public Activity getActivity(Context context) {
        return (Activity) context;
    }

    @OnClick(R.id.welcome_textbar_bu_add)
    public void onClickAdd() {
        String text = etAdd.getText().toString();
        Type addedType = new Type(text);

        if (!text.isEmpty()) {
            if (!getPresenter().isInTypeList(addedType, typeList)) {
                getPresenter().addTypeItem(text, typeList, addedType, db);
                getPresenter().updateRecycler(recycler);
                getPresenter().updateAdditionBarText(etAdd);
                getPresenter().updateShowNextAction(typeList);
            } else {
                showIsMemeberMessage(addedType);
            }
        } else {
            showEmptyFieldMessage();
        }
    }

    @OnClick(R.id.welcome_bu_next)
    public void onClickNext() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        typeList = getPresenter().loadData(db);
        getPresenter().updateShowNextAction(typeList);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new TypeAdapter(getPresenter(), db, typeList));
        super.onResume();
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
        //buNext.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
        buNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNextButton() {
        //buNext.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down));
        buNext.setVisibility(View.GONE);
    }
}
