package com.malpo.bheard.ui.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.malpo.bheard.R;
import com.malpo.bheard.adapters.AlbumsAdapter;
import com.malpo.bheard.models.Album;
import com.malpo.bheard.models.Artist;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

// In this case, the fragment displays simple text based on the page
public class AlbumsFragment extends BaseTabFragment implements AlbumsAdapter.OnAlbumSelectedListener{

    public static final String ARG_ARTIST = "ARG_ARTIST";

    @Bind(R.id.rv)
    RecyclerView mRecyclerView;

    @Bind(R.id.progress)
    ProgressBar mProgressBar;

    private Artist mArtist;
    private AlbumsAdapter mAlbumsAdapter;

    public static AlbumsFragment newInstance(Artist artist) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTIST, Parcels.wrap(artist));
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mArtist = Parcels.unwrap(getArguments().getParcelable(ARG_ARTIST));
    }

    @Override
    public CharSequence getName() {
        return "Albums";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_tab_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(llm);
        updateData(mArtist);
    }

    @Override
    public void updateData(Artist artist) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        this.mArtist = artist;

        Call<List<Album>> call = search.getAlbums(mArtist.getName());
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Response<List<Album>> response, Retrofit retrofit) {
                List<Album> albums = response.body();
                onArtistSearchFinished(albums);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void onArtistSearchFinished(List<Album> albums){
        if(mAlbumsAdapter == null) {
            mAlbumsAdapter = new AlbumsAdapter(albums, getContext());
            mAlbumsAdapter.setOnAlbumSelectedListener(this);
            mRecyclerView.setAdapter(mAlbumsAdapter);
        } else {
            mAlbumsAdapter.updateData(albums);
        }

        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAlbumSelected(Album album) {

    }
}