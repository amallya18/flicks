package com.codepath.anmallya.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.anmallya.flicks.HttpHelper;
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

        setStatusBarColor();

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
                (new HttpHelper()).displayVideoOkHttp(DetailActivity.this, movie.getId(), 0);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HttpHelper()).displayVideoOkHttp(DetailActivity.this, movie.getId(), 1);
            }
        });
    }

    private void setStatusBarColor(){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
    }
}
