package com.codepath.anmallya.flicks.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by anmallya on 10/15/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieList {
    private ArrayList<Movie> results;
    private int page;

    public MovieList(){

    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
