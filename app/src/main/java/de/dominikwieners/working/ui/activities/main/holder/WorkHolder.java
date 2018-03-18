package de.dominikwieners.working.ui.activities.main.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.R;

/**
 * Created by dominikwieners on 18.03.18.
 */

public class WorkHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_working_hour_date)
    TextView tvDate;

    @BindView(R.id.item_working_hour_type)
    TextView tvType;

    @BindView(R.id.item_working_hour_time_begin)
    TextView tvBegin;

    @BindView(R.id.item_working_hour_time_end)
    TextView tvEnd;

    public WorkHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public TextView getTvType() {
        return tvType;
    }

    public TextView getTvBegin() {
        return tvBegin;
    }

    public TextView getTvEnd() {
        return tvEnd;
    }
}
