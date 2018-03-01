package com.example.android.mypopularmoviesapp.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mypopularmoviesapp.Model.Movie;
import com.example.android.mypopularmoviesapp.R;
import com.example.android.mypopularmoviesapp.Rest.ApiManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tonyukuk on 26.02.2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    // for item click
    private static OnItemClickListener mListener;
    private List<Movie> movies;
    private int rowLayout;
    private Context context;


    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


        String image_url = ApiManager.BASE_URL_IMAGE_POSTER + movies.get(position).getPoster_path();
        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.img_placeholder_dark)
                .error(R.drawable.img_placeholder_dark)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // for item click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            // for item click
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
