package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class YoutubeMovie extends Movie {

    String key;
    String site;
    String type;
    Integer size;


    public YoutubeMovie() {
    }


    public YoutubeMovie(Movie movie,JSONObject youTubeObject) throws JSONException {
        super(movie);
        key = youTubeObject.getString("key");
        site = youTubeObject.getString("site");;
        type = youTubeObject.getString("type");;
        size = youTubeObject.getInt("size");
    }


    public static ArrayList<YoutubeMovie> fromJSONArray(JSONArray movies, Movie movie) throws JSONException {
        ArrayList<YoutubeMovie> arrayList = new ArrayList<>();
        for(int i =0; i< movies.length();i++){
            YoutubeMovie youtubeMovie = new YoutubeMovie(movie, movies.getJSONObject(i));
            arrayList.add(youtubeMovie);
        }
        return arrayList;
    }

    public static YoutubeMovie filterMovies(ArrayList<YoutubeMovie>movies){
        //filter for movies with site as "Youtube" and with highest size
        Integer size = 0;
        YoutubeMovie result  = null;
        for(YoutubeMovie temp : movies){
            if(temp.site.equals("YouTube") && temp.size > size){
                result = temp;
                size = temp.size;
            }
        }
        return result;
    }


    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }


    @Override
    public String toString() {
        return "YoutubeMovie{" +
                "key='" + key + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}
