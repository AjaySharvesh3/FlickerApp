package com.sharvesh.flick.flicker.api;

import com.sharvesh.flick.flicker.model.CastResponses;
import com.sharvesh.flick.flicker.model.MovieResponse;
import com.sharvesh.flick.flicker.model.ReviewResponses;
import com.sharvesh.flick.flicker.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpComingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getTrailer(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/casts")
    Call<CastResponses> getCasts(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponses> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieResponse(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("vi/{key}/3.jpg")
    Call<TrailerResponse> getTrailerImage(@Path("key") String key);

}
