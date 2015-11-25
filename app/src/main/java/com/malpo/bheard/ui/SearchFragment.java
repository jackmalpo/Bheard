package com.malpo.bheard.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.ArtistMethod;
import com.malpo.bheard.networking.lastfm.artist.ArtistSearch;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Jack on 9/19/15.
 */
public class SearchFragment extends Fragment {

    @Bind(R.id.search_box) EditText searchBox;
    @Bind(R.id.search_progress) ProgressBar progressBar;

    public static final String TAG = SearchFragment.class.getSimpleName();

    private OnArtistResultListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String artist = searchBox.getText().toString();
                    searchArtist(artist);
                    return false;
                }
                return true;
            }
        });

//        ArtistSearch search = new ArtistSearch("Andrew");
//        Call<List<Artist>> call = search.getSearchResults();
//        call.enqueue(new Callback<List<Artist>>() {
//            @Override
//            public void onResponse(Response<List<Artist>> response, Retrofit retrofit) {
//                List<Artist> artists = response.body();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Throwable x = t;
//            }
//        });
    }


    private void searchArtist(String artist){
        if(!artist.equals("")) {

            ArtistSearch search = new ArtistSearch(artist);
            Call<List<Artist>> call = search.getSearchResults();
            call.enqueue(new Callback<List<Artist>>() {
                @Override
                public void onResponse(Response<List<Artist>> response, Retrofit retrofit) {
                    List<Artist> artists = response.body();
                }

                @Override
                public void onFailure(Throwable t) {
                    Throwable x = t;
                }
            });



        }
    }

    public void setOnArtistResultListener(OnArtistResultListener listener){
        this.listener = listener;
    }

    public interface OnArtistResultListener{
        void onArtistSearchResult(Artist artist);
    }

    //    private void updateProgressBar(boolean visible){
//        int visibility = progressBar.getVisibility();
//        if(visibility == View.GONE || visibility == View.INVISIBLE && visible){
//            searchBox.setVisibility(View.GONE);
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
//    }
}
