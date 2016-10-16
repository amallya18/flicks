package com.codepath.anmallya.flicks.activity;


import android.os.Bundle;
import android.view.View;

import com.codepath.anmallya.flicks.R;
import com.codepath.anmallya.flicks.utils.Constants;
import com.codepath.anmallya.flicks.utils.Key;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YoutubeActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        final String key = getIntent().getStringExtra(Constants.YOUTUBE_VIDEO_KEY);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(Key.YOUTUBE_API,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(key);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}