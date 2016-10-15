package com.codepath.anmallya.flicks.model;

import com.codepath.anmallya.flicks.utils.Url;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by anmallya on 10/15/2016.
 */
public class Movie {

    public Movie(){

    }

    private String posterPath;
    private Boolean isAdult;
    private String overview;
    private String releaseDate;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backDropPath;
    private float popularity;
    private int voteCount;
    private boolean isVideo;

    @JsonProperty("genre_ids")
    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    private List<Integer> genre_ids;

    @JsonProperty("vote_average")
    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    private float voteAverage;

    @JsonProperty("poster_path")
    public String getPosterPath() {
        return Url.getImageUrl(posterPath);
    }

    //@JsonProperty("poster_path")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @JsonProperty("adult")
    public Boolean getAdult() {
        return isAdult;
    }

    public void setAdult(Boolean adult) {
        isAdult = adult;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("original_title")
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @JsonProperty("original_language")
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("backdrop_path")
    public String getBackDropPath() {
        return Url.getImageUrl(backDropPath);
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    @JsonProperty("popularity")
    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    @JsonProperty("vote_count")
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }


    @JsonProperty("video")
    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
