package de.dominikwieners.working.ui.activities.main.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.presenter.ActivityMainPresenter;
import de.dominikwieners.working.presenter.FragmentWorkPresenter;
import de.dominikwieners.working.ui.activities.main.holder.WorkHolder;

/**
 * Created by dominikwieners on 18.03.18.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkHolder> {

    private FragmentWorkPresenter presenter;
    private ArrayList<Work> typeList;

    public WorkAdapter(FragmentWorkPresenter presenter, Bundle bundle) {
        this.typeList = (ArrayList<Work>) bundle.getSerializable(Config.WORK_ITEM_LIST);
        this.presenter = presenter;
    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_working_hour, parent, false);
        return new WorkHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkHolder holder, int position) {
        Work work = typeList.get(position);
        holder.getTvDate().setText(presenter.getDateFromatWidthDay(work.getDayOfWeek(), work.getDay(), work.getMonth(), work.getYear()));
        holder.getTvType().setText(work.getWorkType());
        holder.getTvBegin().setText(presenter.getTimeFormat(work.getStartHour(), work.getStartMin()));
        holder.getTvEnd().setText(presenter.getTimeFormat(work.getEndHour(), work.getEndMin()));
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}
