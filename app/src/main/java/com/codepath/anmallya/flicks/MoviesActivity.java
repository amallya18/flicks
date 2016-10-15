package com.codepath.anmallya.flicks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.codepath.anmallya.flicks.adapter.MovieArrayAdapter;
import com.codepath.anmallya.flicks.model.Movie;
import com.codepath.anmallya.flicks.model.MovieList;
import com.codepath.anmallya.flicks.utils.Url;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MoviesActivity extends AppCompatActivity {

    ArrayList<Movie> movieList = null;
    MovieArrayAdapter movieAdapter;
    ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        lvMovies = (ListView) findViewById(R.id.lv_movie);
        movieList = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this , movieList);
        lvMovies.setAdapter(movieAdapter);
        fetchMovies();
    }


   private void fetchMovies(){
       AsyncHttpClient client = new AsyncHttpClient();
       client.get(Url.getNowPlaying(), new JsonHttpResponseHandler(){

           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               super.onSuccess(statusCode, headers, response);
               JSONArray movieJsonArray = null;
               try {
                   MovieList movieListNew = (new ObjectMapper()).readValue(response.toString(), MovieList.class);
                   System.out.println(movieListNew.getResults().get(0).getOverview());
                   System.out.println(movieListNew.getResults().get(1).getOverview());
                   movieList.addAll(movieListNew.getResults());
                   movieAdapter.notifyDataSetChanged();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
               super.onFailure(statusCode, headers, responseString, throwable);
           }

       });
   }
}
