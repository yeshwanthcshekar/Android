package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import javax.xml.namespace.QName;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageURL;

    public static User fromJSONObject(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = "@" + jsonObject.getString("screen_name");
        user.profileImageURL = jsonObject.getString("profile_image_url_https");
        return user;

    }

    // empty constructor needed by the Parceler library
    public User() {}

}
