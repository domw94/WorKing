package de.dominikwieners.working.ui.activities.welcome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.dominikwieners.working.R;
import de.dominikwieners.working.data.room.Type;
import de.dominikwieners.working.presenter.welcome.ActivityWelcomePresenter;
import de.dominikwieners.working.ui.activities.welcome.holder.TypeHolder;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeHolder> {

    private List<Type> typeList;
    private ActivityWelcomePresenter presenter;
    private Context context;


    public TypeAdapter(ActivityWelcomePresenter presenter, Context context, List<Type> typeList) {
        this.presenter = presenter;
        this.typeList = typeList;
        this.context = context;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_welcome_working_type, parent, false);
        return new TypeHolder(view);
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, int position) {
        final Type type = typeList.get(position);
        holder.getTvType().setText(type.getType());
        holder.getBuType().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteTypeData(context, type);
                typeList.remove(type);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

}
