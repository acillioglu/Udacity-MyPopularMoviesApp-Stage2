package com.example.android.mypopularmoviesapp.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
// import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RELEASEDATE = "releasedate";
    public static final String EXTRA_BACKDROPPATH = "backdroppath";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_VOTEAVERAGE = "voteavarage";
    public static final String EXTRA_POSTERPATH = "posterpath";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        TextView mTvOverview = (TextView) findViewById(R.id.tv_overview);
        TextView mTvReleaseDate = (TextView) findViewById(R.id.tv_releasedate);
        TextView mTvVoteAverage = (TextView) findViewById(R.id.tv_voteaverage);
        ImageView ivBackDropImage = (ImageView) findViewById(R.id.iv_backdrop);
        ImageView ivPosterImage = (ImageView) findViewById(R.id.iv_posterimage);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_title);


        Bundle bundle = getIntent().getExtras();
        String mTitle = bundle.getString(EXTRA_TITLE);
        String mReleasedate = bundle.getString(EXTRA_RELEASEDATE);
        String mBackDropPath = bundle.getString(EXTRA_BACKDROPPATH);
        String mOverview = bundle.getString(EXTRA_OVERVIEW);
        String mVoteAverage = String.valueOf(bundle.getDouble(EXTRA_VOTEAVERAGE));
        String mPosterPath = bundle.getString(EXTRA_POSTERPATH);

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

        String image_url2 = ApiManager.BASE_URL_IMAGE_POSTER + mPosterPath;
        Picasso.with(this)
                .load(image_url2)
                .placeholder(R.drawable.img_placeholder_dark)
                .error(R.drawable.img_placeholder_dark)
                .into(ivPosterImage);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
}
