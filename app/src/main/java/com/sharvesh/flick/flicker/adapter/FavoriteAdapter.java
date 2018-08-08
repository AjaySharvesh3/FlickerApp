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
import com.sharvesh.flick.flicker.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{

    private Context context;
    private List<Movies> moviesList;

    public FavoriteAdapter(Context context, List<Movies> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_cards, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        holder.name.setText(moviesList.get(position).getOriginalTitle());
        String average_vote = Double.toString(moviesList.get(position).getVoteAverage());
        holder.vote.setText(average_vote);

        Picasso.with(context)
                .load(moviesList.get(position).getPosterPath())
                .placeholder(R.drawable.gifload)
                .into(holder.poster_thumbnail);

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.anim_bottom);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView poster_thumbnail;
        TextView name;
        TextView vote;

        FavoriteViewHolder(View itemView) {
            super(itemView);

            poster_thumbnail = itemView.findViewById(R.id.poster_character);
            name = itemView.findViewById(R.id.movie_title);
            vote = itemView.findViewById(R.id.user_rating);
        }
    }


}
