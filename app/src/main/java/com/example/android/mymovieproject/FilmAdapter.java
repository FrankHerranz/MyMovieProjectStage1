package com.example.android.mymovieproject;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.android.mymovieproject.dataDAO.FilmDAO;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Created by luisherranzjerez on 02/02/2017.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmAdapterViewHolder> {

    private FilmDAO[] filmsData;

    private final FilmAdapterOnClickHandler mClickHandler;

    public interface FilmAdapterOnClickHandler {
        void onClick(int actualPositionOfFilm);
    }

    public FilmAdapter(FilmAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class FilmAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView filmThumbnailView;
        public final View localView;

        public FilmAdapterViewHolder(View itemView) {
            super(itemView);
            filmThumbnailView = (ImageView)itemView.findViewById(R.id.film_image);
            localView = itemView;
            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex){
            Picasso.with(localView.getContext()).setIndicatorsEnabled(true);
            Picasso.with(localView.getContext()).load(Uri.parse("http://image.tmdb.org/t/p/w500" + filmsData[listIndex].getUrl_movie_poster())).into(filmThumbnailView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    @Override
    public FilmAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,parent,false);
        FilmAdapterViewHolder viewHolder = new FilmAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilmAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(null == filmsData)
            return 0;
        return filmsData.length;
    }

    public void setFilmsData(FilmDAO[] filmsDataReceived){
        filmsData = filmsDataReceived;
        notifyDataSetChanged();
    }
}
