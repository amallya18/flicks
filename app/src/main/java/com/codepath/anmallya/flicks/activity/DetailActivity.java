package com.codepath.anmallya.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.anmallya.flicks.R;
import com.codepath.anmallya.flicks.model.Movie;
import com.codepath.anmallya.flicks.model.Trailer;
import com.codepath.anmallya.flicks.model.TrailerList;
import com.codepath.anmallya.flicks.utils.Url;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        final Movie movie = (Movie)getIntent().getSerializableExtra("MOVIE");

        RatingBar ratingBar = (RatingBar)findViewById(R.id.rating_bar);

        int numberOfStars = ((int)(movie.getVoteAverage()/2) == 0)? 1: (int)(movie.getVoteAverage()/2);

        ratingBar.setNumStars(numberOfStars);


        System.out.println("Number of stars: "+(int)(movie.getVoteAverage()/2));

        TextView tvVoteCount = (TextView)findViewById(R.id.tv_vote_count);
        tvVoteCount.setText("Votes: "+movie.getVoteCount());

        TextView tvPopularity = (TextView)findViewById(R.id.tv_popularity);
        tvPopularity.setText("Popularity: "+movie.getPopularity());

        TextView tvOverview = (TextView)findViewById(R.id.tv_overview);
        tvOverview.setText(movie.getOverview());

        TextView tvTitle = (TextView)findViewById(R.id.tv_title);
        tvTitle.setText(movie.getOriginalTitle());

        ImageView ivBackDrop = (ImageView)findViewById(R.id.iv_backdrop);
        Picasso.with(this).load(movie.getBackDropPath()).placeholder(R.drawable.placeholder).into(ivBackDrop);

        ivBackDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayVideo(movie.getId(), 0);
            }
        });

        Button btnPlay = (Button)findViewById(R.id.btn_play_trailer);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayVideo(movie.getId(), 1);
            }
        });

        ImageView ivPoster = (ImageView)findViewById(R.id.iv_movie_image);
        Picasso.with(this).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.placeholder).into(ivPoster);
    }

    private void displayVideo(int id, final int type){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Url.getTrailerUrl(String.valueOf(id)), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray TrailerArray = null;
                try {
                    TrailerList trailerList = (new ObjectMapper()).readValue(response.toString(), TrailerList.class);
                    Trailer trailer = trailerList.getResults().get(type);

                    Intent intent = new Intent(DetailActivity.this, YoutubeActivity.class);
                    intent.putExtra("YOUTUBE_VIDEO_KEY", trailer.getKey());
                    startActivity(intent);
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
