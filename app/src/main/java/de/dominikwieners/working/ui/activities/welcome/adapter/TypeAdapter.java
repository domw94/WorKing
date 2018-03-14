package de.dominikwieners.working.ui.activities.welcome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.presenter.WelcomePresenter;
import de.dominikwieners.working.ui.activities.welcome.holder.TypeHolder;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeHolder> {

    private List<Type> typeList;
    private WelcomePresenter presenter;
    private Context context;


    public TypeAdapter(WelcomePresenter presenter, Context context, List<Type> typeList) {
        this.presenter = presenter;
        this.typeList = typeList;
        this.context = context;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_working_type, parent, false);
        return new TypeHolder(view);
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, int position) {
        final Type type = typeList.get(position);
        holder.getTvType().setText(type.getType());
        holder.getTvDot().setText(Html.fromHtml("&#8226;"));
        holder.getTvDot().setTextColor(type.getColor());
        holder.getBuType().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeList.remove(type);
                presenter.delete(context, type);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

}
