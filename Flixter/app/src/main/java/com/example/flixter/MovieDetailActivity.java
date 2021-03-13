package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;
import com.example.flixter.models.YoutubeMovie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieDetailActivity extends YouTubeBaseActivity {

    Movie movie;
    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    YouTubePlayerView youTubePlayerView;
    private final int POPULAR_MOVIE_VOTE_AVERAGE = 7;


    public final String MOVIE_BASE_URL_ONE = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public final String MOVIE_URL = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // resolve the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage =  findViewById(R.id.rbVoteAverage);
        youTubePlayerView = findViewById(R.id.player);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverView());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(String.format(MOVIE_BASE_URL_ONE, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                //Log.d( MOVIE_URL, "SUCCESS !!");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if(jsonArray.length()==0)return;
                    //Construct List of YouTube movies using JSONArray
                    ArrayList<YoutubeMovie> youtubeMovieList = YoutubeMovie.fromJSONArray(jsonArray,movie);
                    //Filter for a movie with site as Youtube and highest size
                    YoutubeMovie filteredMovie = YoutubeMovie.filterMovies(youtubeMovieList);
                    //String youTubeKey = jsonArray.getJSONObject(0).getString("key");

                    initialiseYouTube(filteredMovie);
                    Log.i("Movie ID",filteredMovie.getKey());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(MOVIE_URL, throwable.getMessage());
            }
        });





    }

    private void initialiseYouTube(final YoutubeMovie youTubeMovie){
        youTubePlayerView.initialize("AIzaSyArW70BRGZmCBASQBNLl5H3zxUWxd2_W9Q",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        if(youTubeMovie.getVoteAverage()>=POPULAR_MOVIE_VOTE_AVERAGE)youTubePlayer.loadVideo(youTubeMovie.getKey());
                        else youTubePlayer.cueVideo(youTubeMovie.getKey());
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

}

