package com.sharvesh.flick.flicker.Fragments;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sharvesh.flick.flicker.BuildConfig;
import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.adapter.MovieAdapter;
import com.sharvesh.flick.flicker.api.Client;
import com.sharvesh.flick.flicker.api.Services;
import com.sharvesh.flick.flicker.model.MovieResponse;
import com.sharvesh.flick.flicker.model.Movies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedMovieFragment extends Fragment{

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movies> moviesList;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    View view;

    public TopRatedMovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_rated, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching data...");
        progressDialog.setTitle("Wait a second...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recycler_view);
        moviesList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), moviesList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        loadJsonData();
    }

    private void loadJsonData() {

        try{
            if(BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty()) {
                Toast.makeText(getContext(), "Invalid API key!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            Client client = new Client();
            Services services = Client.getClient().create(Services.class);
            retrofit2.Call<MovieResponse> call = services.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(retrofit2.Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movies> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                    Log.d("Error: ", t.getMessage());
                    Toast.makeText(getContext(), "Oops! Fetching Error, Try Again!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
