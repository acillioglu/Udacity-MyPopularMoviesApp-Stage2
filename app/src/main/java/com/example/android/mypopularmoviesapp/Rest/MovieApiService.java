package com.example.android.mypopularmoviesapp.Rest;

import com.example.android.mypopularmoviesapp.Model.MovieResponse;
import com.example.android.mypopularmoviesapp.Model.MovieReviewResponse;
import com.example.android.mypopularmoviesapp.Model.MovieTrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tonyukuk on 26.02.2018.
 */

public interface MovieApiService {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewResponse> getMovieReview(@Path("id") int id, @Query("api_key") String apiKey);


}
