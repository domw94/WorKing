package de.dominikwieners.working.ui.activities.monthDetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.dominikwieners.working.R;
import de.dominikwieners.working.presenter.monthDetail.MonthDetailPresenter;
import de.dominikwieners.working.ui.activities.monthDetail.holder.MonthDetailHolder;

/**
 * Created by dominikwieners on 09.04.18.
 */

public class MonthDetailAdapter extends RecyclerView.Adapter<MonthDetailHolder> {

    List<String> types;
    Context context;
    MonthDetailPresenter presenter;
    int month;
    int year;

    public MonthDetailAdapter(List<String> types, Context context, MonthDetailPresenter presenter, int month, int year) {
        this.types = types;
        this.context = context;
        this.presenter = presenter;
        this.month = month;
        this.year = year;
    }

    @Override
    public MonthDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_detail, parent, false);
        return new MonthDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthDetailHolder holder, int position) {
        String type = types.get(position);
        holder.getTvType().setText(type);
        holder.getTvHours().setText(String.format(context.getResources().getString(R.string.item_month_detail_hour), presenter.getSumOfWorkingByTypeInHours(context, type, year, month)));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
