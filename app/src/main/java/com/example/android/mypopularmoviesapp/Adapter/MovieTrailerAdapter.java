package com.example.android.mypopularmoviesapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mypopularmoviesapp.Model.MovieTrailer;
import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tonyukuk on 9.03.2018.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private static final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";
    private static OnItemClickListener mListener;
    private List<MovieTrailer> mMovieTrailer;
    private int rowLayout;
    private Context context;


    public MovieTrailerAdapter(List<MovieTrailer> mMovieTrailer, int rowLayout, Context context) {
        this.mMovieTrailer = mMovieTrailer;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }


    @Override
    public MovieTrailerAdapter.MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie_trailer, parent, false);

        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.MovieTrailerViewHolder holder, int position) {

        MovieTrailer movieTrailer = mMovieTrailer.get(position);


        String trailerTumbUrl = YOUTUBE_IMAGE_URL_PREFIX + movieTrailer.getKey() + YOUTUBE_IMAGE_URL_SUFFIX;
        Picasso.with(context)
                .load(trailerTumbUrl)
                .placeholder(R.drawable.img_placeholder_dark)
                .error(R.drawable.img_placeholder_dark)
                .into(holder.mIvTrailer);


    }

    @Override
    public int getItemCount() {
        return mMovieTrailer.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mIvTrailer;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);


            mIvTrailer = (ImageView) itemView.findViewById(R.id.ivTrailer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}
