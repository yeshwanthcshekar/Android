package com.example.flixter.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.MainActivity;
import com.example.flixter.MovieDetailActivity;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    //List<Object> movies;
    List<Movie>movies;

    private final int MOVIE = 0;
    private final int POPULAR_MOVIE = 1;
    private final int POPULAR_MOVIE_VOTE_AVERAGE = 7;


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
        if(movies.get(position).getVoteAverage() >= POPULAR_MOVIE_VOTE_AVERAGE){
            return  POPULAR_MOVIE;
        }else if(movies.get(position).getVoteAverage() < POPULAR_MOVIE_VOTE_AVERAGE)
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
    public class ViewHolderMovie extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtMovieTitle;
        TextView txtMovieOverView;
        ImageView imgMoviePoster;
        ImageView videoPlayButton;


        public ViewHolderMovie(@NonNull View itemView) {
            super(itemView);
            txtMovieTitle = itemView.findViewById(R.id.txtMovieTitle);
            txtMovieOverView = itemView.findViewById(R.id.txtMovieOverview);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);

            videoPlayButton = itemView.findViewById(R.id.imgVideoPreviewPlayButton);
            itemView.setOnClickListener(this::onClick);
        }

        public void bindMovieData(Movie movie) {
            txtMovieOverView.setText(movie.getOverView());
            txtMovieTitle.setText(movie.getTitle());
            String imageURL;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL =  movie.getBackdropPath();
            }else   imageURL = movie.getPosterPath();
            //videoPlayButton.setVisibility(View.VISIBLE);

            Glide.with(context).load(imageURL).centerInside().placeholder(R.drawable.new_york_skyline).into(imgMoviePoster);
           // overlayImage(R.drawable.layered_list);
        }

        @Override
        public void onClick(View view) {
            //Get the position & ensure it’s valid
            int adapterPosition = getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION){
                //Get the Movie at that position in the list
                Movie movie = movies.get(adapterPosition);
               //Create an Intent to display MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailActivity.class);
                //Pass the movie as an extra serialized via Parcels.wrap()
                intent.putExtra(Movie.class.getSimpleName(),Parcels.wrap(movie));

                Pair<View, String> p1 = Pair.create((View)txtMovieTitle, "transMovieTitle");
                Pair<View, String> p2 = Pair.create(txtMovieOverView, "transMovieOverview");
                Pair<View, String> p3 = Pair.create((View)imgMoviePoster, "transImgMovie");

                ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, p1,p2,p3);
                //Show the activity
                context.startActivity(intent, options.toBundle());
            }
        }
        private void overlayImage(int resourceId) {

            ViewOverlay viewOverlay = imgMoviePoster.getOverlay();
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), resourceId, null);
            Rect bounds = drawable.getBounds();
            bounds.set(0,0,0,0);

            /*drawable.setBounds(); = Rect(
                    0,
                    0,
                    target.width,
                    target.height
            )*/
            viewOverlay.add(drawable);
        }
    }

    //ViewHolder class for movies with higher rating
    public class ViewHolderPopularMovie extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgPopularMovie;

        public ViewHolderPopularMovie(@NonNull View itemView) {
            super(itemView);
            imgPopularMovie = itemView.findViewById(R.id.imgBackdropImage);
            itemView.setOnClickListener(this::onClick);
        }

        public void bindPopularMovieData(Movie movie){
            Glide.with(context).load(movie.getBackdropPath()).fitCenter().placeholder(R.drawable.new_york_skyline).into(imgPopularMovie);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(Movie.class.getSimpleName(),Parcels.wrap(movie));

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, imgPopularMovie, "transImgMovie");
                context.startActivity(intent,options.toBundle());
            }
        }

    }

}
