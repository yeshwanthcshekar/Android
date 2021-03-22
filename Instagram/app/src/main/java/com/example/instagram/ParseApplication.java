package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        // Register parse models
        ParseObject.registerSubclass(Post.class);


        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5lZJVhAG3hnA8LjiZbOYIc7IfA5vm8hjNh3okOMz") // should correspond to Application Id env variable
                .clientKey("LV0oxgBU3IDH3sGO3gGI5OeHigNSkTDpuDBe2NnB")  // should correspond to Client key env variable
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
