package com.malpo.bheard.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jack on 12/20/15.
 */
public class SimilarArtistAdapter extends RecyclerView.Adapter<SimilarArtistAdapter.PersonViewHolder>{

    private List<Artist> artists;
    private Context context;
    private OnArtistSelectedListener listener;

    public SimilarArtistAdapter(List<Artist> artists, Context context){
        this.artists = artists;
        this.context = context;
    }

    public void setOnArtistSelectedListener(OnArtistSelectedListener listener){
        this.listener = listener;
    }

    public void updateData(List<Artist> artists){
        this.artists = artists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_recycler_view, viewGroup, false);
        return new PersonViewHolder(v, listener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        Artist artist = artists.get(i);
        personViewHolder.bindArtist(artist, context);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.cv) CardView cv;
        @Bind(R.id.person_name) TextView personName;
        @Bind(R.id.person_photo) ImageView personPhoto;

        private Artist artist;
        private OnArtistSelectedListener listener;

        PersonViewHolder(View itemView, OnArtistSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindArtist(Artist artist, Context context){
            this.artist = artist;
            personName.setText(artist.getName());
            Picasso.with(context).load(artist.getListImageUrl()).fit().centerCrop().into(personPhoto);
        }

        @OnClick(R.id.cv)
        public void onClick(View v){
            listener.onArtistSelected(artist);
        }
    }

    public interface OnArtistSelectedListener{
        void onArtistSelected(Artist artist);
    }

}
