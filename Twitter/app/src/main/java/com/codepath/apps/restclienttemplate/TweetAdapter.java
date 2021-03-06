package com.codepath.apps.restclienttemplate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    //Pass in the context and List of tweets
    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //For each row, inflate the layout for tweet
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tweet_item,parent,false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at the position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    //Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtUserName;
        ImageView imgProfileImage;
        TextView txtScreenName;
        TextView txtTweetBody;
        TextView txtCreatedAt;
        ImageView imgMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfileImage = itemView.findViewById(R.id.imgUserImage);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtTweetBody = itemView.findViewById(R.id.txtTweetBody);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            txtScreenName = itemView.findViewById(R.id.txtScreenName);
            imgMedia = itemView.findViewById(R.id.imgMedia);

        }

        public void bind(Tweet tweet){
            txtUserName.setText(tweet.user.name);
            txtTweetBody.setText(tweet.body);
            txtCreatedAt.setText(tweet.createdAt);
            txtScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageURL).into(imgProfileImage);
            //Glide.with(context).load(tweet.media.get(0).displayURL).into(imgMedia);
        }

    }



}
