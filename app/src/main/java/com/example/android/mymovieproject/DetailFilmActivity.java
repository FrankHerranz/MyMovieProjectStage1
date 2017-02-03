package com.example.android.mymovieproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mymovieproject.dataDAO.FilmDAO;
import com.squareup.picasso.Picasso;

public class DetailFilmActivity extends AppCompatActivity {

    private String title;
    private String release_date;
    private String url_movie_poster;
    private double vote_average;
    private String plot_synopsis;

    private ImageView filmThumbnail;
    private TextView vTitle;
    private TextView vReleaseDate;
    private TextView vVoteAverage;
    private TextView vPlot_synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);



        filmThumbnail = (ImageView)findViewById(R.id.movie_poster);
        vTitle = (TextView)findViewById(R.id.title_film);
        vReleaseDate = (TextView)findViewById(R.id.release_date);
        vVoteAverage = (TextView)findViewById(R.id.vote_average);
        vPlot_synopsis = (TextView)findViewById(R.id.plot_synopsis);

        receiveInfoFromIntent();
        loadDataIntoView();

    }

    public void receiveInfoFromIntent(){
        Intent received = getIntent();
        if(received.hasExtra("Title")) {
            title = received.getStringExtra("Title");
        }
        if(received.hasExtra("Release date")) {
            release_date = received.getStringExtra("Release date");
        }
        if(received.hasExtra("Movie poster")) {
            url_movie_poster = received.getStringExtra("Movie poster");
        }
        if(received.hasExtra("Vote average")) {
            vote_average = received.getDoubleExtra("Vote average", -1);
        }
        if(received.hasExtra("Plot synopsis")) {
            plot_synopsis = received.getStringExtra("Plot synopsis");
        }
    }

    public void loadDataIntoView(){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        Picasso.with(DetailFilmActivity.this).load(Uri.parse("http://image.tmdb.org/t/p/w342" + url_movie_poster)).into(filmThumbnail);
        vTitle.setText(title);
        vReleaseDate.setText("Release Date: " + release_date);
        vVoteAverage.setText("Vote Average: " + Double.toString(vote_average));
        vPlot_synopsis.setText(plot_synopsis);

    }
}
