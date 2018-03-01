package com.example.android.mypopularmoviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.mypopularmoviesapp.Adapter.MoviesAdapter;
import com.example.android.mypopularmoviesapp.Model.MovieResponse;
import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.example.android.mypopularmoviesapp.Model.Movie;
import com.example.android.mypopularmoviesapp.Rest.MovieApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {

    // for Detail intent
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RELEASEDATE = "releasedate";
    public static final String EXTRA_BACKDROPPATH = "backdroppath";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_VOTEAVERAGE = "voteavarage";
    public static final String EXTRA_POSTERPATH = "posterpath";
    private static Retrofit retrofit = null;
    public List<Movie> movies;
    private RecyclerView recyclerView = null;
    private boolean sortPopular = true;
    private ArrayList<Movie> mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // check internet connection
        if (isOnline() == true) {
            connectAndGetApiData(true);
        } else {
            showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);
        }


    }

    public void connectAndGetApiData(boolean isPopular) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiManager.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);


        if (isPopular == true) {

            Call<MovieResponse> call = movieApiService.getPopularMovies(ApiManager.getApiKey());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                    movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.row_movie, getApplicationContext()));


                    // for item click
                    MoviesAdapter.setOnItemClickListener(MainActivity.this);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });

        } else {
            Call<MovieResponse> call = movieApiService.getTopRatedMovies(ApiManager.getApiKey());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                    movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.row_movie, getApplicationContext()));

                    // for item click
                    MoviesAdapter.setOnItemClickListener(MainActivity.this);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.sort_by_popular) {

            sortPopular = true;
            // check internet connection
            if (isOnline() == true) {
                connectAndGetApiData(sortPopular);
            } else {
                showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);
            }

        } else if (itemThatWasClickedId == R.id.sort_by_rated) {
            sortPopular = false;
            connectAndGetApiData(sortPopular);

            if (isOnline() == true) {
                connectAndGetApiData(sortPopular);
            } else {
                showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);
            }


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        Intent detailIntent = new Intent(this, DetailActivity.class);

        Movie clickedItem = movies.get(position);

        detailIntent.putExtra(EXTRA_TITLE, clickedItem.getTitle());
        detailIntent.putExtra(EXTRA_RELEASEDATE, clickedItem.getRelease_date());
        detailIntent.putExtra(EXTRA_BACKDROPPATH, clickedItem.getBackdrop_path());
        detailIntent.putExtra(EXTRA_OVERVIEW, clickedItem.getOverview());
        detailIntent.putExtra(EXTRA_VOTEAVERAGE, clickedItem.getVote_average());
        detailIntent.putExtra(EXTRA_POSTERPATH, clickedItem.getPoster_path());


        startActivity(detailIntent);


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void showSnackbar(View view, String message, int duration) {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        snackbar.setAction(getResources().getString(R.string.main_str_tryagain), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() != true) {
                    showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);

                } else {
                    snackbar.dismiss();
                }

            }
        });

        snackbar.show();
    }


}