package com.example.splendidavocado.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.splendidavocado.flixster.models.Config;
import com.example.splendidavocado.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants
    // the base url for the api

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the paramemter name for the API key
    public final static String API_KEY_PARAM = "api_key";



    public final static String TAG = "MovieActivity";


    // instance fields
    AsyncHttpClient client;



    // the list of currentply playing movies
    ArrayList<Movie> movies;

    // the recycvler view
    RecyclerView rvMovies;
    // adapter wired to thr recycler view
    MovieAdapter adapter;


    // image config
    Config config;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize client
        client = new AsyncHttpClient();

        // initialize the list of movies
        movies = new ArrayList<>();

        // initialize the adapter, movie array cannot be reitinialized
        adapter = new MovieAdapter(movies);
        // resolvce the recycler view and connect a layoyr manager
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        getConfiguration();

    }

    // get the list of currently playing movies from the API
    private void getNowPlaying()
    {
        // create the url
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // execute a GET request expecting a JSON object response

        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // load the results into movies list
                        try {
                            JSONArray results = response.getJSONArray("results");

                            // iterate through the result set and create Movie objects
                            for (int i = 0; i < results.length(); i++)
                            {
                                Movie movie = new Movie(results.getJSONObject(i));
                                movies.add(movie);
                                adapter.notifyItemInserted(movies.size() - 1);

                            }
                            Log.i(TAG, String.format("Loaded %s movies", results.length()));

                        } catch (JSONException e) {
                            logError("Failed to parse now playing movies", e, true);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        logError("Failed to get data from now_playing endpoint", throwable, true);
                    }
                }
        );
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
                    config = new Config(response);

                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", config.getImageBaseUrl(), config.getPosterSize()));

                    // pass config to adapter
                    adapter.setConfig(config);
                    getNowPlaying();
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
