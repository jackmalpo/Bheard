package com.malpo.bheard.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.eventbus.SearchResultEvent;
import com.malpo.bheard.eventbus.SearchStartedEvent;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.ArtistSearch;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Jack on 9/19/15.
 */
public class SearchFragment extends Fragment {

    @Bind(R.id.search_box) AutoCompleteTextView searchBox;
    @Bind(R.id.search_progress) ProgressBar progressBar;

    @Inject ArtistSearch search;
    @Inject EventBus bus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showKeyboard();

        String[] temp = new String[]{"Jack", "Blah"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, temp);
        searchBox.setThreshold(1);
        searchBox.setAdapter(arrayAdapter);
    }

    @OnTextChanged(R.id.search_box) void onTextChanged(CharSequence s){
        searchTest(s.toString());
    }


    @OnEditorAction(R.id.search_box) boolean onEditorAction(int actionId){
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String artist = searchBox.getText().toString();
            searchArtist(artist);
            return false;
        }
        return true;
    }

    private void showKeyboard(){
        searchBox.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
    }

    private void searchArtist(String artist){
        if(!artist.equals("")) {

            //post EventBus started event
            bus.post(new SearchStartedEvent());

            //turn on progress bar
            updateProgressBar(true);

            //get arist info from last.fm
            Call<Artist> call = search.getArtistInfo(artist);
            call.enqueue(new Callback<Artist>() {
                @Override
                public void onResponse(Response<Artist> response, Retrofit retrofit) {

                    //turn off progress bar
                    updateProgressBar(false);

                    //post EventBus result event
                    bus.post(new SearchResultEvent(response.body()));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("SEARCH_ERROR", "There was an error searching.", t);
                }
            });
        }
    }

    private void updateProgressBar(boolean visible){
        int visibility = progressBar.getVisibility();
        if(visibility == View.GONE || visibility == View.INVISIBLE && visible){
            searchBox.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void searchTest(String artist){
    }
}
