package com.example.android.mypopularmoviesapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.mypopularmoviesapp.Adapter.MoviesAdapter;
import com.example.android.mypopularmoviesapp.DataSqlite.MovieContentProvider;
import com.example.android.mypopularmoviesapp.DataSqlite.MovieDbContract;
import com.example.android.mypopularmoviesapp.Model.MovieResponse;
import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.example.android.mypopularmoviesapp.Model.Movie;
import com.example.android.mypopularmoviesapp.Rest.MovieApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RELEASEDATE = "releasedate";
    public static final String EXTRA_BACKDROPPATH = "backdroppath";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_VOTEAVERAGE = "voteavarage";
    public static final String EXTRA_POSTERPATH = "posterpath";
    private static final String SIS_FAVORITE = "state_favourite";
    private static final String SIS_POPULAR = "state_popular";
    private static Retrofit retrofit = null;
    private static boolean isFav;
    private List<Movie> movies;
    private List<Movie> mMovie;
    private RecyclerView recyclerView = null;
    private boolean sortPopular = true;
    private static final int LOADER = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }


        if (savedInstanceState != null) {

            isFav = savedInstanceState.getBoolean(SIS_FAVORITE);
            sortPopular = savedInstanceState.getBoolean(SIS_POPULAR);

            if (isFav == true) {
                LoaderManager mLoaderManager = getLoaderManager();
                mLoaderManager.initLoader(LOADER, null, this);
            } else
                connectAndGetApiData(sortPopular);

        }else {
            connectAndGetApiData(true);
        }


    }

    public void connectAndGetApiData(boolean isPopular) {

        isFav = false;

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


                    if (response.isSuccessful()) {

                        movies = response.body().getResults();
                        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.row_movie, getApplicationContext()));
                        MoviesAdapter.setOnItemClickListener(MainActivity.this);

                    }

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    if (t instanceof IOException) {
                        showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);
                    }


                }
            });

        } else {
            Call<MovieResponse> call = movieApiService.getTopRatedMovies(ApiManager.getApiKey());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                    if (response.isSuccessful()) {

                        movies = response.body().getResults();
                        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.row_movie, getApplicationContext()));
                        MoviesAdapter.setOnItemClickListener(MainActivity.this);

                    }

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    if (t instanceof IOException) {
                        showSnackbar(recyclerView, getResources().getString(R.string.main_str_interneterror), Snackbar.LENGTH_INDEFINITE);
                    }

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

        switch (item.getItemId()) {

            case R.id.sort_by_popular:
                sortPopular = true;
                connectAndGetApiData(sortPopular);
                break;
            case R.id.sort_by_rated:
                sortPopular = false;
                connectAndGetApiData(sortPopular);
                break;
            case R.id.sort_by_favourites:
                LoaderManager mLoaderManager = getLoaderManager();
                mLoaderManager.initLoader(LOADER, null, this);
                isFav = true;
                MoviesAdapter.setOnItemClickListener(MainActivity.this);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        Intent detailIntent = new Intent(this, DetailActivity.class);
        Movie clickedItem;


        if (isFav == true) {
            clickedItem = mMovie.get(position);
        } else {
            clickedItem = movies.get(position);
        }

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
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


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch (i) {
            case LOADER:

                Uri favQueryUri = MovieContentProvider.CONTENT_URI;
                return new android.content.CursorLoader(this,
                        favQueryUri,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader not implemented " + LOADER);


        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        mMovie = new ArrayList<>();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, getResources().getString(R.string.main_str_nofavmovie), Toast.LENGTH_SHORT).show();
            return;
        }

        cursor.moveToFirst();

        do {
            int movieId = cursor.getInt(cursor.getColumnIndex(MovieDbContract.COLUMB_MOVIE_ID));
            String movieName = cursor.getString(cursor.getColumnIndex(MovieDbContract.COLUMB_TITLE));
            String moviePlot = cursor.getString(cursor.getColumnIndex(MovieDbContract.COLUMB_OVERVIEW));
            double movieRating = cursor.getDouble(cursor.getColumnIndex(MovieDbContract.COLUMB_AVERAGE_RATING));
            String moviePoster = cursor.getString(cursor.getColumnIndex(MovieDbContract.COLUMB_POSTER_IMAGE));
            String movieDate = cursor.getString(cursor.getColumnIndex(MovieDbContract.COLUMB_RELEASE_DATE));
            String movieBackdrop = cursor.getString(cursor.getColumnIndex(MovieDbContract.COLUMB_BACKDROP_IMAGE));

            mMovie.add(new Movie(movieId, movieRating, movieName, moviePoster, movieBackdrop, moviePlot, movieDate));
        } while (cursor.moveToNext());


        recyclerView.setAdapter(new MoviesAdapter(mMovie, R.layout.row_movie, getApplicationContext()));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SIS_FAVORITE, isFav);
        outState.putBoolean(SIS_POPULAR, sortPopular);

    }
}