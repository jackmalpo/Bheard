package com.malpo.bheard.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jack on 12/20/15.
 */
public class SimilarArtistAdapter extends RecyclerView.Adapter<SimilarArtistAdapter.PersonViewHolder>{

    private List<Artist> artists;
    private Context context;

    public SimilarArtistAdapter(List<Artist> artists, Context context){
        this.artists = artists;
        this.context = context;
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
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(artists.get(i).getName());
        Picasso.with(context).load(artists.get(i).getListImageUrl()).fit().centerCrop().into(personViewHolder.personPhoto);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cv) CardView cv;
        @Bind(R.id.person_name) TextView personName;
        @Bind(R.id.person_photo) ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
