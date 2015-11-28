package com.malpo.bheard.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.models.Artist;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements SearchFragment.OnArtistResultListener{

    private static final String SEARCH_TAG = "search";

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.header_logo) ImageView headerImage;
    @Bind(R.id.artist_name) TextView artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        showSearch();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch();
            }
        });

    }

    private void showSearch(){
        if(findViewById(R.id.content_frame) != null){
            if(getFragmentManager().findFragmentByTag(SEARCH_TAG) == null) {
                SearchFragment search = new SearchFragment();
                search.setOnArtistResultListener(this);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, search, SEARCH_TAG)
                        .commit();
            }
        }
    }

    @Override
    public void onArtistSearchResult(Artist artist) {
        if(artist != null) {
            String url = artist.getHeaderImageUrl();
            String name = artist.getName();
            Picasso.with(this).load(url).fit().centerCrop().into(headerImage);

            artistName.setVisibility(View.VISIBLE);
            artistName.setText(name);
        }
    }
}
