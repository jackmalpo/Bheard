package com.malpo.bheard.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.adapters.TabFragmentPagerAdapter;
import com.malpo.bheard.eventbus.SearchFinishedEvent;
import com.malpo.bheard.eventbus.SearchStartedEvent;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.LastfmSearch;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Stack;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private static final String SEARCH_TAG = "mSearch";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.header_logo)
    ImageView mHeaderImage;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.tab_wrapper)
    LinearLayout mTabWrapper;

    @Inject EventBus mBus;

    @Inject
    LastfmSearch mSearch;

    private TabFragmentPagerAdapter mPagerAdapter;
    private Stack<Artist> previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getComponent().inject(this);

        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);


        mCollapsingToolbarLayout.setTitle("");
        mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mCollapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.colorAccent));

        previous = new Stack<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
    }

    @Override
    protected void onStop() {
        mBus.unregister(this);
        super.onStop();
    }

    @OnClick(R.id.fab)
    public void showSearch(){
        if(getSupportFragmentManager().findFragmentByTag(SEARCH_TAG) != null) {
            SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(SEARCH_TAG);
            if(fragment != null) {
                fragment.searchIfPossible();
            }
        }
    }

    /**
     * Called when new artist mSearch has been started.
     * @param event
     */
    public void onEvent(SearchStartedEvent event){
        mAppBar.setExpanded(true, false);
        mTabWrapper.setVisibility(View.INVISIBLE);

        Artist artist = event.artist;
        if(artist != null){
            updateHeader(artist);
        } else {
            String artistName = event.artistName;

            //get artist info from last.fm
            Call<Artist> call = mSearch.getArtistInfo(artistName);
            call.enqueue(new retrofit.Callback<Artist>() {
                @Override
                public void onResponse(Response<Artist> response, Retrofit retrofit) {
                    Artist artist = response.body();
                    if (artist != null)
                        updateHeader(artist);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("SEARCH_ERROR", "There was an error searching.", t);
                }
            });
        }
    }

    /**
     * Called when new artist has been found.
     * @param event
     */
    public void onEvent(SearchFinishedEvent event){
        mTabWrapper.setVisibility(View.VISIBLE);
        Artist artist = event.artist;
        previous.push(artist);
        setupViewPager(artist);
    }

    private void setupViewPager(Artist artist){
        if(mPagerAdapter == null) {
            mPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), artist);
            mViewPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            mPagerAdapter.updateData(artist);
        }
    }

    private void updateHeader(final Artist artist){
        try {
            String url = artist.getHeaderImageUrl();
            String name = artist.getName();
//            Picasso.with(this).load(url).transform(new CropFaces(this, mHeaderImage.getWidth(), mHeaderImage.getHeight())).into(mHeaderImage);
            Picasso.with(this).load(url).fit().centerCrop().into(mHeaderImage, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) mHeaderImage.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            mBus.post(new SearchFinishedEvent(artist));
                            updateColors(palette);
                        }
                    });
                }

                @Override
                public void onError() {

                }
            });

            mCollapsingToolbarLayout.setTitle(name);

        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void updateColors(Palette palette){
        int primary = ContextCompat.getColor(this, R.color.colorPrimary);
        int primaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        int accent = ContextCompat.getColor(this, R.color.colorAccent);

        //Update Scrim colors for scroll up.
        mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
        mCollapsingToolbarLayout.setExpandedTitleColor(palette.getLightMutedColor(primary));

        mTabLayout.setTabTextColors(palette.getLightMutedColor(primaryDark), palette.getDarkVibrantColor(accent));
        mTabLayout.setSelectedTabIndicatorColor(palette.getDarkVibrantColor(primaryDark));

        mFloatingActionButton.setBackgroundColor(palette.getDarkVibrantColor(accent));
        mFloatingActionButton.setRippleColor(palette.getDarkMutedColor(primary));

    }

    @Override
    public void onBackPressed() {
        if(previous.size() > 1) {
            previous.pop();
            if(previous.peek() != null) {
                mBus.post(new SearchStartedEvent(previous.pop()));
            }
        } else {
            super.onBackPressed();
        }
    }
}
