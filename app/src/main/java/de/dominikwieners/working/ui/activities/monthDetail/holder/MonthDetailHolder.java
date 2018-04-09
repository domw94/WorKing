package de.dominikwieners.working.ui.activities.monthDetail.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.R;

/**
 * Created by dominikwieners on 09.04.18.
 */

public class MonthDetailHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_month_detail_tv_type)
    TextView tvType;

    @BindView(R.id.item_month_detail_tv_hours)
    TextView tvHours;

    public MonthDetailHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTvType() {
        return tvType;
    }

    public void setTvType(TextView tvType) {
        this.tvType = tvType;
    }

    public TextView getTvHours() {
        return tvHours;
    }

    public void setTvHours(TextView tvHours) {
        this.tvHours = tvHours;
    }
}
