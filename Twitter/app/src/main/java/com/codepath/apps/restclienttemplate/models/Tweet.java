package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public List<Media> media;
    public long tweetId;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = TimeFormatter.getTimeDifference(jsonObject.getString("created_at"));
        tweet.user = User.fromJSONObject(jsonObject.getJSONObject("user"));
        tweet.tweetId = jsonObject.getLong("id");
        //JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
        //tweet.media = Media.fromJSONArray(extendedEntities.getJSONArray("media"));
        return tweet;
    }


    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0 ; i <jsonArray.length();i++){
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }


}
