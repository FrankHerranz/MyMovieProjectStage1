package com.example.android.mymovieproject.dataDAO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luisherranzjerez on 03/02/2017.
 */

public class FilmDAO {

    private int id;
    private String title;
    private String release_date;
    private String url_movie_poster;
    private double vote_average;
    private String plot_synopsis;

    public FilmDAO(int identification){
        id = identification;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getUrl_movie_poster() {
        return url_movie_poster;
    }

    public void setUrl_movie_poster(String url_movie_poster) {
        this.url_movie_poster = url_movie_poster;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

}
