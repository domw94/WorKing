package de.dominikwieners.working.ui.activities.settings.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.dominikwieners.working.Navigator;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.local.SettingsType;
import de.dominikwieners.working.data.room.Type;
import de.dominikwieners.working.ui.activities.settings.SettingsActivity;
import de.dominikwieners.working.ui.activities.settings.holder.SettingsHolder;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;

/**
 * Created by dominikwieners on 28.03.18.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsHolder> {

    private List<SettingsType> list;
    private Context context;
    private Navigator navigator;

    public SettingsAdapter(List<SettingsType> list, Context context, Navigator navigator) {
        this.list = list;
        this.context = context;
        this.navigator = navigator;
    }

    @Override
    public SettingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false);
        return new SettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingsHolder holder, int position) {
        final SettingsType settingsType = list.get(position);
        holder.getIvType().setImageDrawable(context.getResources().getDrawable(settingsType.getDrawableId()));
        holder.getTvType().setText(settingsType.getSettingsDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (settingsType.getId()) {
                    case 1:
                        navigator.showWelcomeActivity((Activity) v.getContext());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
