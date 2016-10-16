package com.codepath.anmallya.flicks.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.anmallya.flicks.R;
import com.codepath.anmallya.flicks.activity.DetailActivity;
import com.codepath.anmallya.flicks.activity.YoutubeActivity;
import com.codepath.anmallya.flicks.model.Movie;
import com.codepath.anmallya.flicks.model.MovieList;
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
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by anmallya on 10/15/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie>{
    ArrayList<Movie> movieList;

    private Context mContext;

    public MovieArrayAdapter(Context context, ArrayList<Movie> movieList)
    {
        super(context, 0, movieList);
        this.movieList = movieList;
        mContext = context;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = (Movie)movieList.get(position);
        return movie.isPopular()? 1: 0;
    }

    private static class ViewHolder{
        ImageView ivImage;
        TextView tvHeader;
        TextView tvOverview;
    }

    private static class ViewHolderPopular{
        ImageView ivImagePopular;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = (Movie)movieList.get(position);
        ViewHolder viewHolder;
        ViewHolderPopular viewHolderPopular;

        if(convertView == null){

            if(movie.isPopular()){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
                viewHolderPopular = new ViewHolderPopular();
                viewHolderPopular.ivImagePopular = (ImageView) convertView.findViewById(R.id.iv_movie_image_popular);
                convertView.setTag(viewHolderPopular);
            } else{
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_movie_image);
                viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.tv_header);
                viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tv_overview);
                convertView.setTag(viewHolder);
            }
        }


        if(movie.isPopular()){
            viewHolderPopular = (ViewHolderPopular) convertView.getTag();
            ImageView ivImage = viewHolderPopular.ivImagePopular;
            ivImage.setTag(movie);
            Picasso.with(getContext()).load(movie.getBackDropPath()).transform(new RoundedCornersTransformation(3, 3)).placeholder(R.drawable.placeholder).into(ivImage);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayVideo(((Movie)v.getTag()).getId());
                }
            });
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
            ImageView ivImage = viewHolder.ivImage;
            ivImage.setTag(movie);
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("MOVIE", (Movie)v.getTag());
                    getContext().startActivity(intent);
                }
            });

            int orientation = getContext().getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getBackDropPath()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.placeholder).into(ivImage);
            } else {
                Picasso.with(getContext()).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.placeholder).into(ivImage);
            }

            TextView tvHeader = viewHolder.tvHeader;
            tvHeader.setText(movie.getOriginalTitle());

            TextView tvOverview = viewHolder.tvOverview;
            tvOverview.setText(movie.getOverview());

        }
        return convertView;
    }


    private void displayVideo(int id){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Url.getTrailerUrl(String.valueOf(id)), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray TrailerArray = null;
                try {
                    TrailerList trailerList = (new ObjectMapper()).readValue(response.toString(), TrailerList.class);
                    Trailer trailer = trailerList.getResults().get(0);

                    Intent intent = new Intent(getContext(), YoutubeActivity.class);
                    intent.putExtra("YOUTUBE_VIDEO_KEY", trailer.getKey());
                    getContext().startActivity(intent);
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
