package com.codepath.anmallya.flicks.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by anmallya on 10/15/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrailerList {
    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }

    private ArrayList<Trailer> results;
}
