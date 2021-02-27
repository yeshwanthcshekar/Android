package com.example.flixter.models;

import com.example.flixter.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String posterPath;
    String overView;
    String title;
    String posterBaseURL;
    String backdropPath;
    Double voteAverage;
    Integer id;


    public Movie() {
    }

    //constructor to construct movie object
    public Movie(JSONObject jsonObject, String thePosterBaseURL) throws JSONException {

        //getting each field with specific key as in JSON data
        posterPath = jsonObject.getString("poster_path");
        overView = jsonObject.getString("overview");
        title = jsonObject.getString("title");
        posterBaseURL = thePosterBaseURL;
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");

    }

    public Movie(Movie movie){
        posterPath= movie.getPosterPath();
        overView = movie.getOverView();
        title = movie.getTitle();
        posterBaseURL = movie.getPosterBaseURL();
        backdropPath = movie.getBackdropPath();
        voteAverage = movie.getVoteAverage();
        id = movie.getId();
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

    public String getPosterBaseURL() {
        return posterBaseURL;
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "posterPath='" + posterPath + '\'' +
                ", overView='" + overView + '\'' +
                ", title='" + title + '\'' +
                ", posterBaseURL='" + posterBaseURL + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", id=" + id +
                '}';
    }
}
