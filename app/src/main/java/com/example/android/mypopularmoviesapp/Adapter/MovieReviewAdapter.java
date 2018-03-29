package com.example.android.mypopularmoviesapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mypopularmoviesapp.Model.MovieReview;
import com.example.android.mypopularmoviesapp.R;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by Tonyukuk on 12.03.2018.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<MovieReview> mMovieReview;
    private int rowLayout;
    private Context context;

    public MovieReviewAdapter(List<MovieReview> mMovieReview, int rowLayout, Context context) {
        this.mMovieReview = mMovieReview;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MovieReviewAdapter.MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie_review, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.MovieReviewViewHolder holder, int position) {

        MovieReview movieReview = mMovieReview.get(position);
        holder.mTvReviewAuthor.setText(movieReview.getAuthor());
        holder.mTvReviewContent.setText(movieReview.getContent());


    }

    @Override
    public int getItemCount() {
        return mMovieReview.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvReviewAuthor;
        public TextView mTvReviewContent;


        public MovieReviewViewHolder(View itemView) {
            super(itemView);

            this.mTvReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            this.mTvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }


}
