package com.malpo.bheard.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.eventbus.SearchResultEvent;
import com.malpo.bheard.eventbus.SearchStartedEvent;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.picasso.CropFaces;
import com.malpo.bheard.tabs.SampleFragmentPagerAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class HomeActivity extends AppCompatActivity {

    private static final String SEARCH_TAG = "search";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.header_logo)
    ImageView headerImage;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Inject EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getComponent().inject(this);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setTitle(null);
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        collapsingToolbarLayout.setTitle("");

        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.colorAccent));

//        showSearch();

        viewPager.setAdapter(new SampleFragmentPagerAdapter(
                getSupportFragmentManager(),
                HomeActivity.this));

        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    @OnClick(R.id.fab)
    public void showSearch(){
//        if(findViewById(R.id.content_frame) != null){
//            SearchFragment search;
//            if(getSupportFragmentManager().findFragmentByTag(SEARCH_TAG) == null) {
//                search = new SearchFragment();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, search, SEARCH_TAG)
//                        .commit();
//                getSupportFragmentManager().executePendingTransactions();
//            } else{
//                search = (SearchFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_TAG);
//                search.searchIfPossible();
//            }
//        }
    }

    /**
     * Called when new artist search has been started.
     * @param event
     */
    public void onEvent(SearchStartedEvent event){
    }

    /**
     * Called when new artist has been found.
     * @param event
     */
    public void onEvent(SearchResultEvent event){
        Artist artist = event.artist;
        try {
            String url = artist.getHeaderImageUrl();
            String name = artist.getName();
//            Picasso.with(this).load(url).transform(new CropFaces(this, headerImage.getWidth(), headerImage.getHeight())).into(headerImage);
            Picasso.with(this).load(url).fit().centerCrop().into(headerImage, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) headerImage.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int primaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
                            int primary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                            collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                            collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
                        }
                    });
                }

                @Override
                public void onError() {

                }
            });

            collapsingToolbarLayout.setTitle(name);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
