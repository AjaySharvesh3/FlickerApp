package com.sharvesh.flick.flicker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.adapter.MovieAdapter;
import com.sharvesh.flick.flicker.database.FavoriteDbHelper;
import com.sharvesh.flick.flicker.model.Movies;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Movies> moviesList;
    FavoriteDbHelper favoriteDbHelper;
    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initiateFavoriteViews();
    }

    private void initiateFavoriteViews() {
        recyclerView = findViewById(R.id.recycler_view_favorite);
        moviesList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, moviesList);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(FavoriteActivity.this);
        getAllFavoriteMovies();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFavoriteMovies() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                moviesList.clear();
                moviesList.addAll(favoriteDbHelper.getAllFavoriteMovieList());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                movieAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
