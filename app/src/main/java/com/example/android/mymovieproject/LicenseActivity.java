package com.example.android.mymovieproject;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        Context context = LicenseActivity.this;
        ImageView imageTheMovieDb = (ImageView)findViewById(R.id.image_themoviedb);
        Picasso.with(context).load(Uri.parse("https://www.themoviedb.org/assets/static_cache/fd6543b66d4fd736a628af57a75bbfda/images/v4/logos/293x302-powered-by-square-blue.png"))
                .into(imageTheMovieDb);
    }
}
