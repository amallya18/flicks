package com.codepath.anmallya.flicks.utils;

/**
 * Created by anmallya on 10/15/2016.
 */
public class Url {

    private final static String API_KEY = "?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final static String NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing"+API_KEY;
    private final static String TRAILER_BEGIN = "https://api.themoviedb.org/3/movie/";
    private final static String TRAILER_END = "/videos"+API_KEY;
    private final static String IMAGES_BASE = "http://image.tmdb.org/t/p/";
    private final static String IMAGE_SIZE = "w780/";

    public static String getNowPlaying() {
        return NOW_PLAYING;
    }

    public static String getImageUrl(String imageName){
        return IMAGES_BASE+IMAGE_SIZE+imageName+API_KEY;
    }

    public static String getTrailerUrl(String movieId){
        return TRAILER_BEGIN + movieId + TRAILER_END;
    }
}
