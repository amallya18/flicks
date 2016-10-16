package com.codepath.anmallya.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.anmallya.flicks.R;
import com.codepath.anmallya.flicks.model.Movie;
import com.codepath.anmallya.flicks.model.Trailer;
import com.codepath.anmallya.flicks.model.TrailerList;
import com.codepath.anmallya.flicks.utils.Constants;
import com.codepath.anmallya.flicks.utils.Url;
import com.codepath.anmallya.flicks.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity  {

    @BindView(R.id.rating_bar) RatingBar ratingBar;
    @BindView(R.id.tv_vote_count) TextView tvVoteCount;
    @BindView(R.id.tv_popularity) TextView tvPopularity;
    @BindView(R.id.tv_language) TextView tvLanguage;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.iv_backdrop) ImageView ivBackDrop;
    @BindView(R.id.iv_movie_image) ImageView ivPoster;
    @BindView(R.id.btn_play_trailer) Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        final Movie movie = (Movie)getIntent().getSerializableExtra(Constants.MOVIE_INFO);
        int numberOfStars = ((int)(movie.getVoteAverage()/2) == 0)? 1: (int)(movie.getVoteAverage()/2);

        ratingBar.setNumStars(numberOfStars);
        tvVoteCount.setText("Votes: "+ movie.getVoteCount());
        tvPopularity.setText("Popularity: "+ Utils.round(movie.getPopularity(), 2));
        tvLanguage.setText("Language: "+movie.getOriginalLanguage());
        tvReleaseDate.setText("Release Date: "+movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());
        tvTitle.setText(movie.getOriginalTitle());

        Picasso.with(this).load(movie.getBackDropPath()).placeholder(R.drawable.placeholder).into(ivBackDrop);
        Picasso.with(this).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.placeholder).into(ivPoster);

        ivBackDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayVideoOkHttp(movie.getId(), 0);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayVideoOkHttp(movie.getId(), 1);
            }
        });
    }

    private void displayVideoOkHttp(int id, final int type){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Url.getTrailerUrl(String.valueOf(id)))
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

                    TrailerList trailerList = (new ObjectMapper()).readValue(json.toString(), TrailerList.class);
                    Trailer trailer = trailerList.getResults().get(type);

                    Intent intent = new Intent(DetailActivity.this, YoutubeActivity.class);
                    intent.putExtra(Constants.YOUTUBE_VIDEO_KEY, trailer.getKey());
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    intent.putExtra(Constants.YOUTUBE_VIDEO_KEY, trailer.getKey());
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
