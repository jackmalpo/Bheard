package com.malpo.bheard.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.adapters.SearchDropdownAdapter;
import com.malpo.bheard.eventbus.SearchFinishedEvent;
import com.malpo.bheard.eventbus.SearchStartedEvent;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.LastfmSearch;

import java.util.ArrayList;
import java.util.List;

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
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener{

    @Bind(R.id.search_box) AutoCompleteTextView searchBox;

    @Bind(R.id.search_progress) ProgressBar progressBar;
    @Inject
    LastfmSearch mSearch;

    @Inject EventBus mBus;

    private List<Artist> mSearchText;
    private Call<List<Artist>> mSearchCall;
    private SearchDropdownAdapter mSearchAdapter;

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
    public void onStart() {
        super.onStart();
        if(!mBus.isRegistered(this))
            mBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!mBus.isRegistered(this))
            mBus.register(this);

        if(searchBox != null){
            if(!searchBox.getText().toString().equals("")) {
                searchBox.setText("");
            }
        }

        setupAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBus.isRegistered(this))
            mBus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mBus.isRegistered(this))
            mBus.unregister(this);
    }

    private void setupAdapter(){
        mSearchText = new ArrayList<>();
        mSearchAdapter = new SearchDropdownAdapter(getActivity(), R.layout.search_dropdown_item, mSearchText);
        searchBox.setOnItemClickListener(this);
        searchBox.setAdapter(mSearchAdapter);
    }

    @OnTextChanged(R.id.search_box) void onTextChanged(CharSequence s){
        startDropDownSearch(s.toString());
    }

    private void startDropDownSearch(String artist){
        if(mSearchCall != null){
            mSearchCall.cancel();
        }
        mSearchCall = mSearch.getSearchResults(artist);
        mSearchCall.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Response<List<Artist>> response, Retrofit retrofit) {
                if(response.body() != null) {
                    updateDropdownAdapter(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void updateDropdownAdapter(List<Artist> response){
        mSearchAdapter.updateData(response);
    }

    @OnEditorAction(R.id.search_box) boolean onEditorAction(int actionId){
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            String artist = searchBox.getText().toString();

            //post EventBus started event
            mBus.post(new SearchStartedEvent(artist));

            return false;
        }
        return true;
    }

    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        searchBox.requestFocus();
        imm.showSoftInput(searchBox, InputMethodManager.SHOW_FORCED);
    }

    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * Called when new artist mSearch has been started.
     * @param event
     */
    public void onEvent(SearchStartedEvent event){

            hideKeyboard();

            searchBox.setText("");

            //turn on progress bar
            updateProgressBar(true);
    }

    /**
     * Called when new artist has been found.
     * @param event
     */
    public void onEvent(SearchFinishedEvent event){
        updateProgressBar(false);
    }


    private void updateProgressBar(boolean visible){
        try {
            if (visible) {
                searchBox.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                searchBox.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Artist artist = (Artist) parent.getItemAtPosition(position);

        //post EventBus started event
        mBus.post(new SearchStartedEvent(artist));
    }

    public void searchIfPossible(){
        if(searchBox != null) {
            if (searchBox.getVisibility() == View.VISIBLE && !searchBox.getText().toString().equals("")) {

                //post EventBus started event
                mBus.post(new SearchStartedEvent(searchBox.getText().toString()));

            } else {
                searchBox.setText("");
                searchBox.setVisibility(View.VISIBLE);
                showKeyboard();
            }
        }
    }
}
