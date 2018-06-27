package com.example.splendidavocado.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants
    // the base url for the api

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the paramemter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // the API key -- TODO move


    public final static String TAG = "MovieActivity";


    // instance fields
    AsyncHttpClient client;

    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize client
        client = new AsyncHttpClient();

        getConfiguration();
    }
    // get the configuration form the API
    private void getConfiguration() {
        // create the url
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");

                    // get image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    // get the pster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    // use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3, "w342");


                } catch (JSONException e)
                {
                    logError("Failed parsing configuration", e, true);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    // handle errors, log and alert
    private void logError(String message, Throwable error, boolean alertUser)
    {
        Log.e(TAG, message, error);
        // alert the user to avoid silent errors
        if (alertUser)
        {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        }

    }
}
