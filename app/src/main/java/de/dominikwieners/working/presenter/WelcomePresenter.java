package de.dominikwieners.working.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.view.WelcomeView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class WelcomePresenter extends MvpBasePresenter<WelcomeView> {

    public void addTypeItem(String text, List<Type> types, Type type, TypeDatabase db) {
        types.add(type);
        db.getTypeDao().insertAll(type);
    }

    public void updateRecycler(RecyclerView recycler) {
        recycler.getAdapter().notifyDataSetChanged();
    }

    public void updateAdditionBarText(EditText etAdd) {
        etAdd.setText(null);
    }

    public List<Type> loadData(TypeDatabase db) {
        return db.getTypeDao().getAll();
    }

    public void updateShowNextAction(List<Type> typeList) {
        if (!typeList.isEmpty()) {
            ifViewAttached(new ViewAction<WelcomeView>() {
                @Override
                public void run(@NonNull WelcomeView view) {
                    view.showNextButton();
                }
            });
        } else {
            ifViewAttached(new ViewAction<WelcomeView>() {
                @Override
                public void run(@NonNull WelcomeView view) {
                    view.hideNextButton();
                }
            });
        }
    }

    public boolean isInTypeList(Type addedType, List<Type> typeList) {
        for (Type type : typeList) {
            if (type.getType().equals(addedType.getType())) {
                return true;
            }
        }
        return false;
    }
}
