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
import com.malpo.bheard.models.Album;
import com.malpo.bheard.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jack on 12/20/15.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>{

    private List<Album> albums;
    private Context context;
    private OnAlbumSelectedListener listener;

    public AlbumsAdapter(List<Album> albums, Context context){
        this.albums = albums;
        this.context = context;
    }

    public void setOnAlbumSelectedListener(OnAlbumSelectedListener listener){
        this.listener = listener;
    }

    public void updateData(List<Album> albums){
        this.albums = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_recycler_view, viewGroup, false);
        return new AlbumViewHolder(v, listener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder personViewHolder, int i) {
        Album album = albums.get(i);
        personViewHolder.bindAlbum(album, context);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.cv) CardView cv;
        @Bind(R.id.person_name) TextView personName;
        @Bind(R.id.person_photo) ImageView personPhoto;

        private Album album;
        private OnAlbumSelectedListener listener;

        AlbumViewHolder(View itemView, OnAlbumSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindAlbum(Album album, Context context){
            this.album = album;
            if(album.getName() != null)
                personName.setText(album.getName());

            if(album.getListImageUrl() != null && !album.getListImageUrl().equals(""))
                Picasso.with(context).load(album.getListImageUrl()).fit().centerCrop().into(personPhoto);
        }

        @OnClick(R.id.cv)
        public void onClick(View v){
            listener.onAlbumSelected(album);
        }
    }

    public interface OnAlbumSelectedListener {
        void onAlbumSelected(Album album);
    }

}
