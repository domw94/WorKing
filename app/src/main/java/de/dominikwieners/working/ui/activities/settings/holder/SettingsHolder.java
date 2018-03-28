package de.dominikwieners.working.ui.activities.settings.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dominikwieners.working.R;

/**
 * Created by dominikwieners on 28.03.18.
 */

public class SettingsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_settings_edit_types_iv)
    ImageView ivType;

    @BindView(R.id.item_settings_edit_types_tv)
    TextView tvType;

    public SettingsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getIvType() {
        return ivType;
    }

    public TextView getTvType() {
        return tvType;
    }
}
