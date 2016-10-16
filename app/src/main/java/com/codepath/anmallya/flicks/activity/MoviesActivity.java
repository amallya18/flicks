package com.codepath.anmallya.flicks.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.anmallya.flicks.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesActivity extends AppCompatActivity {

    ArrayList<Movie> movieList = null;
    MovieArrayAdapter movieAdapter;

    @BindView(R.id.lv_movie) ListView lvMovies;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        getSupportActionBar().setTitle("Flickster");
        ButterKnife.bind(this);
        movieList = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this , movieList);
        lvMovies.setAdapter(movieAdapter);
        fetchMoviesOkHttp();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesOkHttp();
            }
        });
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
                   //movieList.addAll(movieListNew.getResults());
                   addAll(movieListNew);

                   movieAdapter.notifyDataSetChanged();
                   if(swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing(false);
                   }
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


    private void fetchMoviesOkHttp(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Url.getNowPlaying())
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    MovieList movieListNew = (new ObjectMapper()).readValue(json.toString(), MovieList.class);
                    System.out.println(movieListNew.getResults().get(0).getOverview());
                    System.out.println(movieListNew.getResults().get(1).getOverview());
                    addAll(movieListNew);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movieAdapter.notifyDataSetChanged();
                            if(swipeRefreshLayout != null){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // helps to add movies to the list
    // helps to avoid duplication of movies during swipe to refresh.

    private void addAll(MovieList newMovieList){
        ArrayList<Movie> movieListNew = newMovieList.getResults();
        if(movieList.size() == 0){
            movieList.addAll(movieListNew);
            return;
        }
        Movie first = movieList.get(0);

        for(Movie movie:movieListNew){
            if(movie.getId() == first.getId()){
                break;
            }
            movieList.add(0, movie);
        }
    }
}
