package com.codepath.anmallya.flicks;

import android.app.Activity;
import android.content.Intent;

import com.codepath.anmallya.flicks.activity.YoutubeActivity;
import com.codepath.anmallya.flicks.model.Trailer;
import com.codepath.anmallya.flicks.model.TrailerList;
import com.codepath.anmallya.flicks.utils.Constants;
import com.codepath.anmallya.flicks.utils.Url;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anmallya on 10/16/2016.
 */

/*
  This class helps in fetching the trailer list, getting the video key, and starting the Youtube activity
 */
public class HttpHelper {

    // currently not used
   public void displayVideoAndroidAsyncHttp(final Activity activity, int id, final int type){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Url.getTrailerUrl(String.valueOf(id)), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray TrailerArray = null;
                try {
                    TrailerList trailerList = (new ObjectMapper()).readValue(response.toString(), TrailerList.class);
                    Trailer trailer = trailerList.getResults().get(type);
                    Intent intent = new Intent(activity, YoutubeActivity.class);
                    intent.putExtra(Constants.YOUTUBE_VIDEO_KEY, trailer.getKey());
                    activity.startActivity(intent);
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

    // currently used
    public void displayVideoOkHttp(final Activity activity, int id, final int type){
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

                    Intent intent = new Intent(activity, YoutubeActivity.class);
                    intent.putExtra(Constants.YOUTUBE_VIDEO_KEY, trailer.getKey());
                    activity.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
