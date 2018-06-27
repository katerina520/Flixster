package com.example.splendidavocado.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class MovieListActivity extends AppCompatActivity {

    // constants
    // the base url for the api

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the paramemter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // the API key -- TODO move
    public final static String API_KEY = "f5c1e23f995bf777239c87cf84ae41f8";


    // instance fields
    AsyncHttpClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize client
        client = new AsyncHttpClient();
    }
    // get the configuration form the API
    private void getConfiguration() {
        // create the url
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, API_KEY);
        // execute a GET request
    }
}
