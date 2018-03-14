package de.dominikwieners.working.ui.activities.welcome.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import de.dominikwieners.working.R;
import butterknife.BindView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class TypeHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_welcome_dot)
    TextView tvDot;
    @BindView(R.id.item_working_tv_type)
    TextView tvType;

    @BindView(R.id.item_working_bu_type)
    ImageButton buType;

    public TypeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTvDot() {
        return tvDot;
    }
    public TextView getTvType() {
        return tvType;
    }

    public ImageButton getBuType() {
        return buType;
    }

}
