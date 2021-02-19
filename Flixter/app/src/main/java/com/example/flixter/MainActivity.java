package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.ComplexRecyclerViewAdapter;
import com.example.flixter.adapters.*;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public final String TAG = "MainActivity";
    public final String PosterTAG = "PosterPath";
    public String base_url;
    public final String POSTER_PATH_URL="https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    List<Movie> movieList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Async call to fetch baseURL
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(POSTER_PATH_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(PosterTAG,"Poster Path Successful");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject jsonImagesObject = jsonObject.getJSONObject("images");
                    JSONArray poster_sizes = jsonImagesObject.getJSONArray("poster_sizes");
                    base_url = jsonImagesObject.getString("secure_base_url");
                    Log.i(PosterTAG, "Poster Base URL" + base_url);
                    Log.i(PosterTAG, "Poster Sizes" + poster_sizes.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(PosterTAG,"Poster Path Failure", throwable);
            }
        });

        RecyclerView rvMovies = findViewById(R.id.rvMovie);

        //Create the adapter
        ComplexRecyclerViewAdapter movieAdapter = new ComplexRecyclerViewAdapter(this,movieList);


        //Set the adapter to the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager to the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Async call to fetch movies
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "success");
                JSONObject jsonObject =  json.jsonObject;
                try{
                    JSONArray results = jsonObject.getJSONArray("results");
                    movieList.addAll(Movie.fromJsonArray(results, base_url));
                    movieAdapter.notifyDataSetChanged();
                    //String posterPath = movieList.get(0).getPosterPath();
                    Log.i(TAG, "Results" + movieList.size());

                }catch (JSONException e){
                    Log.e(TAG,"Hit json exception",e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"Failure");

            }
        });
    }
}