package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    Tweet tweet;

    TextView txtTweetDetailFullName;
    ImageView imgTweetDetailUserImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // unwrap the tweet passed in via intent, using its simple name as a key
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

    }
}