package com.example.splendidavocado.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {


    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;

    // backdrop size
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");

        // get image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get the pster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse backdrop sizes
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");


    }


    // helper method

    public String getImageUrl(String size, String path)
    {

        return String.format("%s%s%s", imageBaseUrl, size, path);
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
