package com.example.android.mymovieproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.mymovieproject.dataDAO.FilmDAO;
import com.example.android.mymovieproject.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements FilmAdapter.FilmAdapterOnClickHandler {

    private ProgressBar mLoadingIndicator;
    private int sortOrderMovies=0;
    private int pageMovies=1;

    public static final int NUM_LIST_ITEMS = 20;
    FilmAdapter mFilmAdapter;
    RecyclerView nMoviesList;

    FilmDAO[] films = new FilmDAO[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar)findViewById(R.id.bar_loadingContent);

        nMoviesList = (RecyclerView)findViewById(R.id.recyclerview_moviesThumbnails);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        nMoviesList.setLayoutManager(layoutManager);
        nMoviesList.setHasFixedSize(true);
        mFilmAdapter = new FilmAdapter(this);
        nMoviesList.setAdapter(mFilmAdapter);

        Integer[] numbers = new Integer[]{sortOrderMovies, pageMovies};
        new FetchMoviesTask().execute(numbers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void loadMoviesData(){

    }

    @Override
    public void onClick(int actualPositionOfFilm) {

        Context context = MainActivity.this;
        Class destinationActivity = DetailFilmActivity.class;

        Intent intent = new Intent(context,destinationActivity);
        intent.putExtra("Title", films[actualPositionOfFilm].getTitle());
        intent.putExtra("Release date", films[actualPositionOfFilm].getRelease_date());
        intent.putExtra("Movie poster", films[actualPositionOfFilm].getUrl_movie_poster());
        intent.putExtra("Vote average", films[actualPositionOfFilm].getVote_average());
        intent.putExtra("Plot synopsis", films[actualPositionOfFilm].getPlot_synopsis());
        startActivity(intent);

    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, FilmDAO[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected FilmDAO[] doInBackground(Integer... integers) {


            if (integers.length == 0) {
                Log.e("ERROR NULL", "Error in integers.length");
                return null;
            }

            Integer sortOrder = integers[0];
            Integer page = integers[1];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortOrderMovies, page);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                JSONObject movieResults = new JSONObject(jsonMoviesResponse);
                JSONArray moviesObtainedJSONArray = movieResults.getJSONArray("results");


                for(int i=0; i<moviesObtainedJSONArray.length(); i++){
                    JSONObject jsonObject = moviesObtainedJSONArray.getJSONObject(i);

                    //We create a FilmDAO object and fill it with the necessary information.
                    films[i] = new FilmDAO(Integer.parseInt(jsonObject.getString("id")));


                    films[i].setTitle(jsonObject.getString("original_title"));
                    films[i].setRelease_date(jsonObject.getString("release_date"));
                    films[i].setUrl_movie_poster(jsonObject.getString("poster_path"));
                    films[i].setVote_average(Double.parseDouble(jsonObject.getString("vote_average")));
                    films[i].setPlot_synopsis(jsonObject.getString("overview"));
                }
                return films;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FilmDAO[] films) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(films != null){
                //Everything is ok
                Log.i("DATOS", "Informacion de films " + Arrays.toString(films));
                mFilmAdapter.setFilmsData(films);
            }else{
                //An error has occured
                Log.e("ERROR NULL", "Error null in onPostExecute");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();

        switch (itemSelected) {
            case R.id.action_refresh:
                new FetchMoviesTask().execute(new Integer[]{sortOrderMovies, pageMovies});
                break;
            case R.id.action_license:
                startActivity(new Intent(MainActivity.this,LicenseActivity.class));
                break;
            case R.id.action_sortOrderMovies:
                AlertDialog.Builder builderSortMoviesDialog = new AlertDialog.Builder(MainActivity.this);
                builderSortMoviesDialog.setTitle(R.string.sortOrderMovies)
                        .setSingleChoiceItems(new String[]{"By most popular", "By highest rated"}, sortOrderMovies,new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichOrderIsSelected){
                                sortOrderMovies = whichOrderIsSelected;
                                Integer[] numbers = new Integer[]{sortOrderMovies, pageMovies};
                                new FetchMoviesTask().execute(numbers);
                            }
                        } );
                AlertDialog dialogSortOrderToShow = builderSortMoviesDialog.create();
                dialogSortOrderToShow.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
