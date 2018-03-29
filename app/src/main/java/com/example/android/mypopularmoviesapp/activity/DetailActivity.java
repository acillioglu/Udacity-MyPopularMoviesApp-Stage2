package com.example.android.mypopularmoviesapp.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
// import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.example.android.mypopularmoviesapp.Adapter.MovieReviewAdapter;
import com.example.android.mypopularmoviesapp.Adapter.MovieTrailerAdapter;
import com.example.android.mypopularmoviesapp.DataSqlite.MovieContentProvider;
import com.example.android.mypopularmoviesapp.DataSqlite.MovieDbContract;
import com.example.android.mypopularmoviesapp.DataSqlite.MovieDbHelper;
import com.example.android.mypopularmoviesapp.Model.MovieReview;
import com.example.android.mypopularmoviesapp.Model.MovieReviewResponse;
import com.example.android.mypopularmoviesapp.Model.MovieTrailer;
import com.example.android.mypopularmoviesapp.Model.MovieTrailerResponse;
import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.example.android.mypopularmoviesapp.Rest.MovieApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements MovieTrailerAdapter.OnItemClickListener {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RELEASEDATE = "releasedate";
    public static final String EXTRA_BACKDROPPATH = "backdroppath";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_VOTEAVERAGE = "voteavarage";
    public static final String EXTRA_POSTERPATH = "posterpath";

    private static Retrofit retrofit = null;

    public List<MovieTrailer> movieTrailers;
    public List<MovieReview> movieReviews;
    public boolean favourite;
    String mId;
    String mTitle;
    String mReleasedate;
    String mBackDropPath;
    String mOverview;
    String mVoteAverage;
    String mPosterPath;
    FloatingActionButton fab;
    private RecyclerView recyclerView = null;
    private RecyclerView recyclerViewReview = null;
    private SQLiteDatabase movieDb;
    private CoordinatorLayout coordinatorLayout;

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        MovieDbHelper dbHelper = new MovieDbHelper(this);

        movieDb = dbHelper.getWritableDatabase();


        TextView mTvOverview = (TextView) findViewById(R.id.tv_overview);
        TextView mTvReleaseDate = (TextView) findViewById(R.id.tv_releasedate);
        TextView mTvVoteAverage = (TextView) findViewById(R.id.tv_voteaverage);
        ImageView ivBackDropImage = (ImageView) findViewById(R.id.iv_backdrop);
        ImageView ivPosterImage = (ImageView) findViewById(R.id.iv_posterimage);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_title);


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            mId = String.valueOf(bundle.getInt(EXTRA_ID));
            mTitle = bundle.getString(EXTRA_TITLE);
            mReleasedate = bundle.getString(EXTRA_RELEASEDATE);
            mBackDropPath = bundle.getString(EXTRA_BACKDROPPATH);
            mOverview = bundle.getString(EXTRA_OVERVIEW);
            mVoteAverage = String.valueOf(bundle.getDouble(EXTRA_VOTEAVERAGE));
            mPosterPath = bundle.getString(EXTRA_POSTERPATH);


            collapsingToolbarLayout.setTitle(mTitle);


            mTvOverview.setText(mOverview);
            mTvReleaseDate.setText(mReleasedate);
            mTvVoteAverage.setText(mVoteAverage + "/10");


            String image_url = ApiManager.BASE_URL_IMAGE_POSTER + mBackDropPath;

            Picasso.with(this)
                    .load(image_url)
                    .placeholder(R.drawable.img_placeholder_dark)
                    .error(R.drawable.img_placeholder_dark)
                    .into(ivBackDropImage);

            String image_url2 = ApiManager.BASE_URL_IMAGE_BACKDROP + mPosterPath;
            Picasso.with(this)
                    .load(image_url2)
                    .placeholder(R.drawable.img_placeholder_dark)
                    .error(R.drawable.img_placeholder_dark)
                    .into(ivPosterImage);


            favourite = favExists(mId);


        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.rvTrailer);
        recyclerView.setLayoutManager(layoutManager);


        recyclerViewReview = (RecyclerView) findViewById(R.id.rvReview);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReview.setNestedScrollingEnabled(false);

        connectAndGetApiData();

        fab = (FloatingActionButton) findViewById(R.id.fab_fav);

        if (favourite) {
            favourite = true;
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_24dp, null));
        } else {
            favourite = false;
            fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_border_24dp, null));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favourite) {
                    fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_border_24dp, null));
                    deleteFromFavorite();
                } else {
                    fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_24dp, null));
                    addToFavourite();
                }
            }
        });


    }

    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiManager.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);


        Call<MovieTrailerResponse> call = movieApiService.getMovieTrailer(Integer.parseInt(mId), ApiManager.getApiKey());
        call.enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {

                if (response.isSuccessful()) {

                    movieTrailers = response.body().getResults();
                    recyclerView.setAdapter(new MovieTrailerAdapter(movieTrailers, R.layout.row_movie_trailer, getApplicationContext()));
                    MovieTrailerAdapter.setOnItemClickListener(DetailActivity.this);

                }

            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {

            }
        });

        Call<MovieReviewResponse> call2 = movieApiService.getMovieReview(Integer.parseInt(mId), ApiManager.getApiKey());
        call2.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call2, Response<MovieReviewResponse> response2) {

                if (response2.isSuccessful()) {
                    movieReviews = response2.body().getResults();
                    recyclerViewReview.setAdapter(new MovieReviewAdapter(movieReviews, R.layout.row_movie_review, getApplicationContext()));
                }

            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call2, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position) {

        MovieTrailer clickedItem = movieTrailers.get(position);
        String mVideoKey = clickedItem.getKey();
        watchYoutubeVideo(this, mVideoKey);


    }

    public boolean favExists(String id) {

        String selectString = "SELECT * FROM " + MovieDbContract.TABLE_NAME +
                " WHERE " + MovieDbContract.COLUMB_MOVIE_ID +
                " =?";
        Cursor cursor = movieDb.rawQuery(selectString, new String[]{id});
        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }

        }
        cursor.close();
        return hasObject;


    }

    public void addToFavourite() {

        favourite = true;

        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MovieDbContract.COLUMB_MOVIE_ID, mId);
        values.put(MovieDbContract.COLUMB_TITLE, mTitle);
        values.put(MovieDbContract.COLUMB_AVERAGE_RATING, mVoteAverage);
        values.put(MovieDbContract.COLUMB_POSTER_IMAGE, mPosterPath);
        values.put(MovieDbContract.COLUMB_BACKDROP_IMAGE, mBackDropPath);
        values.put(MovieDbContract.COLUMB_OVERVIEW, mOverview);
        values.put(MovieDbContract.COLUMB_RELEASE_DATE, mReleasedate);

        contentResolver.insert(MovieContentProvider.CONTENT_URI, values);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.addedtofavourites, Snackbar.LENGTH_SHORT);

        snackbar.show();


    }

    public void deleteFromFavorite() {

        favourite = false;

        ContentResolver contentResolver = getContentResolver();

        String selection = MovieDbContract.COLUMB_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(mId)};

        contentResolver.delete(MovieContentProvider.CONTENT_URI, selection, selectionArgs);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.removedfromfavourites, Snackbar.LENGTH_SHORT);

        snackbar.show();

    }

}
