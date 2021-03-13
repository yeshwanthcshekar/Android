package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public List<Media> media;
    public long tweetId;
    public int retweetCount;
    public int replyCount;
    public int favoriteCount;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = TimeFormatter.getTimeDifference(jsonObject.getString("created_at"));
        tweet.user = User.fromJSONObject(jsonObject.getJSONObject("user"));
        tweet.tweetId = jsonObject.getLong("id");
        JSONObject extendedEntities = null;
        try{
            extendedEntities = jsonObject.getJSONObject("extended_entities");
        }catch (JSONException jsonException){
            Log.e("MEDIA",jsonException.toString());
        }

        if(extendedEntities!=null)tweet.media = Media.fromJSONArray(extendedEntities.getJSONArray("media"));
        try {
            tweet.retweetCount = jsonObject.getInt("retweet_count");
        }catch (JSONException jsonException){
            Log.e("ReTweet_Count",jsonException.toString());
        }
        try {
            tweet.replyCount = jsonObject.getInt("reply_count");
        }catch (JSONException jsonException){
          Log.e("REPLY_COUNT",jsonException.toString());
        }
        try{
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
        }catch (JSONException jsonException){
            Log.e("Favorite_Count", jsonException.toString());
        }

        return tweet;
    }


    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0 ; i <jsonArray.length();i++){
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    // empty constructor needed by the Parceler library
    public Tweet() {}



}
