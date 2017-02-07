package com.example.android.mymovieproject.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by luisherranzjerez on 02/02/2017.
 */

public class NetworkUtils {

    private final static String APIKEY_PARAM = "api_key";

    private final static String LANGUAGE_PARAM = "language";

    private final static String PAGE_PARAM = "page";


    public static URL buildUrl(Integer sortOrder, Integer page){
        Uri builtUri;
        if(sortOrder == 0){
            builtUri = Uri.parse("https://api.themoviedb.org/3/movie/popular").buildUpon()
                    .appendQueryParameter(APIKEY_PARAM, "adb0dc2df1ec33d5b6d3da3be052aeb7")
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .appendQueryParameter(PAGE_PARAM, Integer.toString(page)).build();
        }
        else{
            builtUri = Uri.parse("https://api.themoviedb.org/3/movie/top_rated").buildUpon()
                    .appendQueryParameter(APIKEY_PARAM, "adb0dc2df1ec33d5b6d3da3be052aeb7")
                    .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                    .appendQueryParameter(PAGE_PARAM, Integer.toString(page)).build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("INFORMACION", "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection == null)
                Log.d("DEBUG INFO", "NUUUUUUUUUULL");

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else{
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
