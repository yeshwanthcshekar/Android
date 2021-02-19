package com.example.flixter.adapters;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    //List<Object> movies;
    List<Movie>movies;

    private final int MOVIE = 0;
    private final int POPULAR_MOVIE = 1;


    //Constructor
    public ComplexRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating the layout from XML and returning the ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case MOVIE:
                View v1 = inflater.inflate(R.layout.movie_item, parent, false);
                viewHolder = new ViewHolderMovie(v1);
                break;

            case POPULAR_MOVIE:
                View v2 = inflater.inflate(R.layout.popular_movie_item,parent,false);
                viewHolder = new ViewHolderPopularMovie(v2);
                break;

            default:
                View v3 = inflater.inflate(R.layout.movie_item, parent, false);
                viewHolder = new ViewHolderMovie(v3);
                break;
        }

        return  viewHolder;
    }


    //returns type of movie
    @Override
    public int getItemViewType(int position) {
        //Movie movie = movies.get(position).getVoteAverage();
        if(movies.get(position).getVoteAverage() >= 7){
            return  POPULAR_MOVIE;
        }else if(movies.get(position).getVoteAverage() < 7)
            return MOVIE;
        return -1;
    }

    //Involves populating the data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Get the movie data at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the ViewHolder
        switch (holder.getItemViewType()){
            case MOVIE:
                ViewHolderMovie movieHolder = (ViewHolderMovie) holder ;
                movieHolder.bindMovieData(movie);
                break;
            case POPULAR_MOVIE:
                ViewHolderPopularMovie popularMovieHolder = (ViewHolderPopularMovie) holder;
                popularMovieHolder.bindPopularMovieData(movie);
                break;

        }
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //ViewHolder class for movies with less rating
    public class ViewHolderMovie extends RecyclerView.ViewHolder {

        TextView txtMovieTitle;
        TextView txtMovieOverView;
        ImageView imgMoviePoster;


        public ViewHolderMovie(@NonNull View itemView) {
            super(itemView);
            txtMovieTitle = itemView.findViewById(R.id.txtMovieTitle);
            txtMovieOverView = itemView.findViewById(R.id.txtMovieOverview);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
        }

        public void bindMovieData(Movie movie) {
            txtMovieOverView.setText(movie.getOverView());
            txtMovieTitle.setText(movie.getTitle());
            String imageURL;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL =  movie.getBackdropPath();
            }else   imageURL = movie.getPosterPath();
            Glide.with(context).load(imageURL).centerInside().placeholder(R.drawable.new_york_skyline).into(imgMoviePoster);
        }
    }

    //ViewHolder class for movies with higher rating
    public class ViewHolderPopularMovie extends RecyclerView.ViewHolder{

        ImageView imgPopularMovie;

        public ViewHolderPopularMovie(@NonNull View itemView) {
            super(itemView);
            imgPopularMovie = itemView.findViewById(R.id.imgBackdropImage);
        }

        public void bindPopularMovieData(Movie movie){
            Glide.with(context).load(movie.getBackdropPath()).fitCenter().placeholder(R.drawable.new_york_skyline).into(imgPopularMovie);
        }

    }

}
