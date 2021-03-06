package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimeLineActivity";
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweetList;
    TweetAdapter tweetAdapter;
    SwipeRefreshLayout swipeContainer;


    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose){
            //Compose icon has been selected
            //Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
            //Navigate to the compose activity
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //Update the RV with the tweet
            //Modify data source of tweets
            tweetList.add(0,tweet);
            //Update the adapter
            tweetAdapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!!");
                populateHomeTimeLine();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        client = TwitterApp.getRestClient(this);
        //find the recyclerView
        rvTweets = findViewById(R.id.rvTweets);

        //initialize the list of tweets and adapter
        tweetList = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this,tweetList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //RecyclerView setup:Layout manager and adapter
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(tweetAdapter);


        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //Triggered only when new data needs to be appended to the list
                Log.i(TAG,"onLoadMore " + page);
                loadMoreData();
            }
        };

        //Adds scroll Listener to the recyclerview
        rvTweets.addOnScrollListener(endlessRecyclerViewScrollListener);
        populateHomeTimeLine();

    }

    private void loadMoreData() {
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.

        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                //  --> Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
                   // tweetAdapter.getItemCount();
                    tweetAdapter.addAll(tweets);
                    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    //tweetAdapter.notifyItemRangeInserted(tweetList.size()-1,tweets.size());
                    Log.i(TAG,"onSuccess for LoadMore Data!! " + "tweetList size = " + (tweetList.size()-1) + "Tweets Size =  " + tweets.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  --> Append the new data objects to the existing set of items inside the array of items
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure for LoadMoreData !! " + throwable);

            }
        }, tweetList.get(tweetList.size()-1).tweetId);

    }

    private void populateHomeTimeLine(){
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"onSuccess!! " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweetAdapter.clear();
                    tweetAdapter.addAll(Tweet.fromJSONArray(jsonArray));
                    //tweetList.addAll(Tweet.fromJSONArray(jsonArray));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                    tweetAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "JSON exception",e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure" + response, throwable);
            }
        });
    }

}