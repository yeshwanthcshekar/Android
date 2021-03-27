package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.PostsFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    //Fragments defined here
    final Fragment composeFragment = new ComposeFragment();
    final Fragment postsFragment = new PostsFragment();
    final Fragment profileFragment = new ProfileFragment();

    //ArrayList<Post> allPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = postsFragment;
                        PostsFragment.smoothScroll();
                        break;
                    case R.id.action_compose:
                        fragment = composeFragment ;
                       // bottomNavigationView.setItemBackgroundResource(R.drawable.states_compose);
                        //item.setChecked(true);
                        break;
                    case R.id.action_profile:
                        fragment = profileFragment ;
                        //bottomNavigationView.setItemBackgroundResource(R.drawable.states_profile);
                        break;
                    default:
                        fragment = postsFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }



   /* private void fetchTimelineAsync(int page) {
            ParseQuery<Post> parseQuery = ParseQuery.getQuery(Post.class);
            parseQuery.include(Post.KEY_USER);
            parseQuery.addDescendingOrder(Post.KEY_CREATED_KEY);
            parseQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> posts, ParseException e) {
                    if(e != null){
                        //Log.e(TAG, " Issue with getting posts",e);
                        return;
                    }
                    *//*for(Post post: posts){
                        // Log.i(TAG, "Post:" +  post.getDescription() + "Username: " + post.getUser().getUsername());
                    }*//*
                    posts.addAll(posts);
                    postsAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
            });

    }*/

}