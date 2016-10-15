package com.codepath.anmallya.flicks.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.anmallya.flicks.R;
import com.codepath.anmallya.flicks.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

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

    /*
    @Override
    public int getViewTypeCount() {
        //return 4;
        //return 500;
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        //return myList.get(position).getAlignment(); // hoping this returns value as 0 and 1
        return myList.get(position).getAlignment().getVal();
        //return position;
    }
    */

    private static class ViewHolder{
        ImageView ivImage;
        TextView tvHeader;
        TextView tvOverview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = (Movie)movieList.get(position);
        ViewHolder viewHolder;

        if(convertView == null){
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_movie, parent, false);

           viewHolder = new ViewHolder();
           viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_movie_image);
            viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.tv_header);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tv_overview);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        ImageView ivImage = viewHolder.ivImage;

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

        return convertView;
    }


}
