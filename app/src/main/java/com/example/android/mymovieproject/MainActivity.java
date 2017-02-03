package com.example.android.mymovieproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        new FetchMoviesTask().execute(new Integer[]{sortOrderMovies, pageMovies});
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
        Context context = this;
        Toast.makeText(context, Integer.toString(actualPositionOfFilm), Toast.LENGTH_SHORT)
                .show();
    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Integer... integers) {

            if (integers.length == 0) {
                Log.e("ERROR NULL", "Error null en integers.length");
                return null;
            }

            Integer sortOrder = integers[0];
            Integer page = integers[1];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortOrderMovies, page);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                JSONObject movieResults = new JSONObject(jsonMoviesResponse);
                JSONArray moviesObtainedJSONArray = movieResults.getJSONArray("results");
                String[] filmImgUrl = new String[20];

                for(int i=0; i<moviesObtainedJSONArray.length(); i++){
                    JSONObject jsonObject = moviesObtainedJSONArray.getJSONObject(i);
                    String moviePath = jsonObject.getString("poster_path");
                    filmImgUrl[i] = "http://image.tmdb.org/t/p/w500" + moviePath;
                }
                return filmImgUrl;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(strings != null){
                //Everything is ok
                Log.i("DATOS", "Informacion de strings " + Arrays.toString(strings));
                mFilmAdapter.setFilmsData(strings);
            }else{
                //An error has occured
                Log.e("ERROR NULL", "Error null en string de onPostExecute");
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
                                new FetchMoviesTask().execute(new Integer[]{sortOrderMovies, pageMovies});
                            }
                        } );
                AlertDialog dialogSortOrderToShow = builderSortMoviesDialog.create();
                dialogSortOrderToShow.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
