package de.dominikwieners.working.presenter;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.view.MainView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    public List<Type> loadData(TypeDatabase db) {
        return db.getTypeDao().getAll();
    }
}
