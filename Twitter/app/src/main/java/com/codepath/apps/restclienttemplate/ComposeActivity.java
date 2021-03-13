package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;
    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";
    TwitterClient client;
    TextView txtCharacterCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        txtCharacterCount = findViewById(R.id.txtCharacterCount);
        etCompose.addTextChangedListener(mTextEditorWatcher);


        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this,"Sorry , your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this,"Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                //Make an API call to twitter to publish tweet
                //Toast.makeText(ComposeActivity.this,tweetContent, Toast.LENGTH_LONG).show();
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJSON(json.jsonObject);
                            Log.i(TAG, "Published tweet says:" + tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            // set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            // closes the activity, pass data to parent
                            finish();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish Tweet");
                    }
                });
            }
        });




    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            if(s.length() >= 280){
                btnTweet.setClickable(false);
            }else
                btnTweet.setClickable(true);
            txtCharacterCount.setText(String.valueOf(s.length()) + "/280");
        }

        public void afterTextChanged(Editable s) {
        }
    };
}