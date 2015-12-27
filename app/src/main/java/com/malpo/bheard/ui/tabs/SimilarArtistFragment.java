package com.malpo.bheard.ui.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.malpo.bheard.R;
import com.malpo.bheard.adapters.SimilarArtistAdapter;
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
public class SimilarArtistFragment extends BaseTabFragment implements SimilarArtistAdapter.OnArtistSelectedListener{

    public static final String ARG_ARTIST = "ARG_ARTIST";

    @Bind(R.id.rv)
    RecyclerView mRecyclerView;

    @Bind(R.id.progress)
    ProgressBar mProgressBar;

    private Artist mArtist;
    private SimilarArtistAdapter mSimilarArtistAdapter;

    public static SimilarArtistFragment newInstance(Artist artist) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTIST, Parcels.wrap(artist));
        SimilarArtistFragment fragment = new SimilarArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SimilarArtistFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mArtist = Parcels.unwrap(getArguments().getParcelable(ARG_ARTIST));
    }

    @Override
    public CharSequence getName() {
        return "Similar";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.similar_artist_fragment, container, false);
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

        GridLayoutManager llm = new GridLayoutManager(view.getContext(), 2);
        mRecyclerView.setLayoutManager(llm);
        updateData(mArtist);
    }

    @Override
    public void updateData(Artist artist) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        this.mArtist = artist;
        Call<List<Artist>> call = search.getSimilarArtists(mArtist.getName());
        call.enqueue(new Callback<List<Artist>>() {

            @Override
            public void onResponse(Response<List<Artist>> response, Retrofit retrofit) {
                if(response.body() != null) {
                    onArtistSearchFinished(response.body());
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void onArtistSearchFinished(List<Artist> artists){
        if(mSimilarArtistAdapter == null) {
            mSimilarArtistAdapter = new SimilarArtistAdapter(artists, getContext());
            mSimilarArtistAdapter.setOnArtistSelectedListener(this);
            mRecyclerView.setAdapter(mSimilarArtistAdapter);
        } else {
            mSimilarArtistAdapter.updateData(artists);
        }

        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onArtistSelected(Artist artist) {
        Toast.makeText(getContext(), artist.getName(), Toast.LENGTH_SHORT).show();
    }
}