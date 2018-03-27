package de.dominikwieners.working.ui.activities.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Work;
import de.dominikwieners.working.presenter.main.FragmentMonthPresenter;
import de.dominikwieners.working.ui.activities.main.holder.WorkHolder;

/**
 * Created by dominikwieners on 18.03.18.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkHolder> {

    private FragmentMonthPresenter presenter;
    private ArrayList<Work> typeList;
    private Context context;

    public WorkAdapter(FragmentMonthPresenter presenter, Context context, Bundle bundle) {
        this.presenter = presenter;
        this.context = context;
        this.typeList = (ArrayList<Work>) bundle.getSerializable(Config.WORK_ITEM_LIST);

    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_working_hour, parent, false);
        return new WorkHolder(view);
    }

    @Override
    public void onBindViewHolder(final WorkHolder holder, int position) {
        final Work work = typeList.get(position);
        holder.getTvDate().setText(presenter.getDateFromatWidthDay(work.getDayOfWeek(), work.getDay(), work.getMonth(), work.getYear()));
        holder.getTvType().setText(work.getWorkType());
        holder.getTvBegin().setText(presenter.getTimeFormat(work.getStartHour(), work.getStartMin()));
        holder.getTvEnd().setText(presenter.getTimeFormat(work.getEndHour(), work.getEndMin()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
                alert.setTitle(R.string.main_delete_entry_dialog_title);
                alert.setMessage(R.string.main_delete_entry_dialog_content);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteWorkData(work, context);
                        typeList.remove(work);
                        notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
}
