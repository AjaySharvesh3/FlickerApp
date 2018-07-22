package com.sharvesh.flick.flicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.model.Casts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private List<Casts> castsList;

    public CastAdapter(Context context, List<Casts> castsList) {
        this.context = context;
        this.castsList = castsList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.casts_cards, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        holder.castChar.setText(castsList.get(position).getCast_character());
        holder.castName.setText(castsList.get(position).getCast_name());
        Picasso.with(context)
                .load(castsList.get(position).getCast_poster())
                .placeholder(R.drawable.cast_icon)
                .into(holder.poster_thumbnail);
    }

    @Override
    public int getItemCount() {
        return castsList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView poster_thumbnail;
        TextView castName;
        TextView castChar;

        CastViewHolder(View itemView) {
            super(itemView);

            poster_thumbnail = itemView.findViewById(R.id.poster_character);
            castName = itemView.findViewById(R.id.cast_name);
            castChar = itemView.findViewById(R.id.cast_char);
        }
    }

}
