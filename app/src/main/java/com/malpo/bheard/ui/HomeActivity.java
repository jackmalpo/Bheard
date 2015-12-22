package com.malpo.bheard.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.R;
import com.malpo.bheard.eventbus.SearchResultEvent;
import com.malpo.bheard.eventbus.SearchStartedEvent;
import com.malpo.bheard.models.Artist;
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
    Toolbar mToolbar;

    @Bind(R.id.header_logo)
    ImageView mHeaderImage;

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Inject EventBus mBus;

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
        updateHeader(artist);
        setupViewPager();

    }

    private void setupViewPager(){
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(
                getSupportFragmentManager(),
                HomeActivity.this));

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void updateHeader(Artist artist){
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

}
