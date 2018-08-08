package com.sharvesh.flick.flicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.activity.DetailActivity;
import com.sharvesh.flick.flicker.activity.MainActivity;
import com.sharvesh.flick.flicker.model.Movies;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movies> movies;

    public MovieAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_cards, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MovieViewHolder viewHolder, int position) {
        viewHolder.movieTitle.setText(movies.get(position).getOriginalTitle());
        String average_vote = Double.toString(movies.get(position).getVoteAverage());
        viewHolder.ratings.setText(average_vote);

        String posterPath = "http://image.tmdb.org/t/p/w185" + movies.get(position).getPosterPath();
        Picasso.with(context)
                .load(posterPath)
                .placeholder(R.drawable.gifload)
                .into(viewHolder.posterThumbnail);

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.anim_bottom);
        viewHolder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitle;
        TextView ratings;
        ImageView posterThumbnail;

        MovieViewHolder(final View view) {
            super(view);
            movieTitle = view.findViewById(R.id.movie_title);
            ratings = view.findViewById(R.id.user_rating);
            posterThumbnail = view.findViewById(R.id.poster_thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int indexPosition = getAdapterPosition();
                    if (indexPosition != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("original_title", movies.get(indexPosition).getOriginalTitle());
                        intent.putExtra("poster_path", movies.get(indexPosition).getPosterPath());
                        intent.putExtra("vote_average", Double.toString(movies.get(indexPosition).getVoteAverage()));
                        intent.putExtra("overview", movies.get(indexPosition).getOverview());
                        intent.putExtra("original_language", movies.get(indexPosition).getOriginalLanguage());
                        intent.putExtra("backdrop_path", movies.get(indexPosition).getBackdropPath());
                        intent.putExtra("release_date", movies.get(indexPosition).getReleaseDate());
                        intent.putExtra("id", movies.get(indexPosition).getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,view.findViewById(R.id.poster_thumbnail), "Flicker");
                        context.startActivity(intent, optionsCompat.toBundle());
                    }
                }
            });
        }

    }

}
