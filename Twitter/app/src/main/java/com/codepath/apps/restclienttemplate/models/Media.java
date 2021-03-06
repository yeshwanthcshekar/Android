package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Media {

    public long id;
    public String mediaURLSecured;
    public String displayURL;
    public String expandedURL;
    public String type;


    public static Media fromJSON(JSONObject jsonObject) throws JSONException {
        Media media = new Media();
        media.id = jsonObject.getLong("id");
        media.mediaURLSecured = jsonObject.getString("media_url_https");
        media.displayURL = jsonObject.getString("display_url");
        media.expandedURL = jsonObject.getString("expanded_url");
        media.type = jsonObject.getString("type");
        return media;
    }


    public static List<Media> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Media> mediaList = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
           mediaList.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return  mediaList;
    }

}
