package com.sharvesh.flick.flicker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.sharvesh.flick.flicker.BuildConfig;
import com.sharvesh.flick.flicker.R;
import com.sharvesh.flick.flicker.adapter.CastAdapter;
import com.sharvesh.flick.flicker.adapter.ReviewAdapter;
import com.sharvesh.flick.flicker.adapter.TrailerAdapter;
import com.sharvesh.flick.flicker.api.Client;
import com.sharvesh.flick.flicker.api.Services;
import com.sharvesh.flick.flicker.database.FavoriteDbHelper;
import com.sharvesh.flick.flicker.model.CastResponses;
import com.sharvesh.flick.flicker.model.Casts;
import com.sharvesh.flick.flicker.model.Movies;
import com.sharvesh.flick.flicker.model.ReviewResponses;
import com.sharvesh.flick.flicker.model.Reviews;
import com.sharvesh.flick.flicker.model.TrailerResponse;
import com.sharvesh.flick.flicker.model.Trailers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView movieName;
    TextView releaseDate;
    TextView voteAverage;
    TextView languageTv;
    TextView overviewTv;
    ImageView posterImage;
    TextView noReviews;
    ImageView backdropImage;
    ImageButton imageButton;

    RecyclerView recyclerViewTrailers, recyclerViewCasts, recyclerViewReviews;

    TrailerAdapter trailerAdapter;
    CastAdapter castAdapter;
    ReviewAdapter reviewAdapter;

    List<Trailers> trailersList;
    List<Casts> castsList;
    List<Reviews> reviewsList;

    CoordinatorLayout coordinatorLayout;

    private FavoriteDbHelper favoriteDbHelper;
    Movies favMovies;
    private final AppCompatActivity activity = DetailActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        movieName = findViewById(R.id.movie_name);
        releaseDate = findViewById(R.id.releaseDate);
        voteAverage = findViewById(R.id.vote);
        languageTv = findViewById(R.id.language);
        overviewTv = findViewById(R.id.overview);
        posterImage = findViewById(R.id.poster);
        backdropImage = findViewById(R.id.backdrop);
        noReviews = findViewById(R.id.noReviews);
        movieName.setSelected(true);

        imageButton = findViewById(R.id.back_button);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.fav_button), "Press favorite button", "It will add to the favorite list.")
                        .tintTarget(false).outerCircleColor(R.color.yellow));

        Intent startIntentActivity = getIntent();
        if (startIntentActivity.hasExtra("original_title")) {
            String movie_name = getIntent().getExtras().getString("original_title");
            String release_date = getIntent().getExtras().getString("release_date");
            String vote_average = getIntent().getExtras().getString("vote_average");
            String language = getIntent().getExtras().getString("original_language");
            String overview = getIntent().getExtras().getString("overview");
            String poster = "http://image.tmdb.org/t/p/w185" + getIntent().getExtras().getString("poster_path");
            String backdrop = "http://image.tmdb.org/t/p/w780" + getIntent().getExtras().getString("backdrop_path");

            movieName.setText(movie_name);
            releaseDate.setText(release_date);
            voteAverage.setText(vote_average);
            languageTv.setText(language);
            overviewTv.setText(overview);

            Picasso.with(this).load(backdrop).placeholder(R.drawable.gifload).into(backdropImage);
            Picasso.with(this).load(poster).placeholder(R.drawable.gifload).into(posterImage);

            collapsingToolbarLayout.setTitle(movie_name);

        } else {
            Toast.makeText(this, "Data not found!", Toast.LENGTH_LONG).show();
        }

        initViewsVideoList();
        initViewsCastList();
        initView_reviews();

        LikeButton likeButton = findViewById(R.id.fav_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                SharedPreferences.Editor editor =
                        getSharedPreferences("com.sharvesh.flick.flicker.activity.DetailActivity", MODE_PRIVATE).edit();
                editor.putBoolean("Favorite Added!", true);
                editor.commit();
                saveAsFavorite();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Added to Favorite", Snackbar.LENGTH_LONG)
                        .setAction("Favorites", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailActivity.this, FavoriteActivity.class);
                                startActivity(intent);
                            }
                        });
                snackbar.setActionTextColor(Color.YELLOW);

                likeButton.setLiked(true);

                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int movie_id = getIntent().getExtras().getInt("id");
                favoriteDbHelper = new FavoriteDbHelper(activity);
                favoriteDbHelper.deleteFavorite(movie_id);
                SharedPreferences.Editor editor =
                        getSharedPreferences("com.sharvesh.flick.flicker.activity.DetailActivity", MODE_PRIVATE).edit();
                editor.putBoolean("Favorite Removed!", true);
                editor.commit();
                Snackbar.make(coordinatorLayout, "Removed from Favorite", Snackbar.LENGTH_SHORT).show();
                likeButton.setLiked(false);
            }
        });

        favoriteDbHelper = new FavoriteDbHelper(this);
        likeButton.setLiked(false);
        List<Movies> moviesListFavs = favoriteDbHelper.getAllFavorite();
        for (int i = 0; i < moviesListFavs.size(); i++) {
            Log.d("Favorite Movies: ", moviesListFavs.get(i).toString());
            if (moviesListFavs.get(i).getOriginalTitle().equals(movieName.getText().toString())) {
                likeButton.setLiked(true);
                break;
            }
        }
    }


    private void initViewsVideoList(){
        trailersList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(getApplicationContext(), trailersList);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailers = findViewById(R.id.recycler_view_trailers);
        recyclerViewTrailers.isNestedScrollingEnabled();
        recyclerViewTrailers.setLayoutManager(mLayoutManager);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
        loadJSON_Trailer();
    }

    private void loadJSON_Trailer(){
        int movie_id = getIntent().getExtras().getInt("id");
        try{
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client();
            Services apiService = Client.getClient().create(Services.class);
            retrofit2.Call<TrailerResponse> callOne = apiService.getTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_KEY);
            callOne.enqueue(new retrofit2.Callback<TrailerResponse>() {
                @Override
                public void onResponse(retrofit2.Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        List<Trailers> trailer = response.body().getResults();
                        recyclerViewTrailers.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                        recyclerViewTrailers.smoothScrollToPosition(0);
                    }
                @Override
                public void onFailure(retrofit2.Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViewsCastList(){
        castsList = new ArrayList<>();
        castAdapter = new CastAdapter(getApplicationContext(), castsList);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCasts = findViewById(R.id.recycler_view_casts);
        recyclerViewCasts.setLayoutManager(mLayoutManager);
        recyclerViewCasts.setAdapter(castAdapter);
        castAdapter.notifyDataSetChanged();
        loadJSON_Casts();
    }

    private void loadJSON_Casts(){
        int movie_id = getIntent().getExtras().getInt("id");
        try{
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            Services apiServices = Client.getClient().create(Services.class);
            retrofit2.Call<CastResponses> call = apiServices.getCasts(movie_id, BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new retrofit2.Callback<CastResponses>() {
                @Override
                public void onResponse(retrofit2.Call<CastResponses> call, Response<CastResponses> response) {
                    List<Casts> castsList = response.body().getResults();
                    recyclerViewCasts.setAdapter(new CastAdapter(getApplicationContext(), castsList));
                    recyclerViewCasts.smoothScrollToPosition(0);
                }
                @Override
                public void onFailure(retrofit2.Call<CastResponses> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView_reviews () {
        reviewsList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewsList);
        LinearLayoutManager staggeredGridLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerViewReviews = findViewById(R.id.recycler_view_reviews);
        recyclerViewReviews.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewReviews.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        loadJSON_reviews();
    }

    private void loadJSON_reviews () {
        int movie_id = getIntent().getExtras().getInt("id");
        try {
            if (BuildConfig.THE_MOVIE_DB_API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            Services apiServices = Client.getClient().create(Services.class);
            retrofit2.Call<ReviewResponses> call = apiServices.getReviews(movie_id, BuildConfig.THE_MOVIE_DB_API_KEY);
            call.enqueue(new retrofit2.Callback<ReviewResponses>() {
                @Override
                public void onResponse(retrofit2.Call<ReviewResponses> call, Response<ReviewResponses> response) {
                    List<Reviews> reviewsList = response.body().getReviewsList();
                    recyclerViewReviews.setAdapter(new ReviewAdapter(getApplicationContext(), reviewsList));
                    recyclerViewReviews.smoothScrollToPosition(0);
                    if (reviewsList.isEmpty()) {
                        noReviews.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<ReviewResponses> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveAsFavorite () {
        favoriteDbHelper = new FavoriteDbHelper(activity);
        favMovies = new Movies();
        int movie_id = getIntent().getExtras().getInt("id");
        String vote = getIntent().getExtras().getString("vote_average");
        String poster = getIntent().getExtras().getString("poster_path");
        favMovies.setId(movie_id);
        favMovies.setOriginalTitle(movieName.getText().toString().trim());
        favMovies.setPosterPath(poster);
        favMovies.setVoteAverage(Double.parseDouble(vote));
        favMovies.setOverview(overviewTv.getText().toString().trim());
        favoriteDbHelper.addFavorite(favMovies);
    }

}
