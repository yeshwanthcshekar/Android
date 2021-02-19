package com.example.flixter.models;

import com.example.flixter.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    String posterPath;
    String overView;
    String title;
    String posterBaseURL;
    String backdropPath;
    double voteAverage;



    //constructor to construct movie object
    public Movie(JSONObject jsonObject, String thePosterBaseURL) throws JSONException {

        //getting each field with specific key as in JSON data
        posterPath = jsonObject.getString("poster_path");
        overView = jsonObject.getString("overview");
        title = jsonObject.getString("title");
        posterBaseURL = thePosterBaseURL;
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");

    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray, String posterBaseURL) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0;i<movieJsonArray.length();i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i), posterBaseURL));

        }
        return movies;
    }

    public String getPosterPath() {
        return posterBaseURL + "w342" + posterPath;
        //return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getOverView() {
        return overView;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return posterBaseURL + "w342" + backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }


}
