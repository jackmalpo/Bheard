package com.malpo.bheard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.malpo.bheard.models.Artist;
import com.malpo.bheard.ui.tabs.BaseTabFragment;
import com.malpo.bheard.ui.tabs.SimilarArtistFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private Artist artist;
    private List<BaseTabFragment> fragmentList;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context, Artist artist) {
        super(fm);
        this.context = context;
        this.artist = artist;
        initializeFragments(artist);
    }

    private void initializeFragments(Artist artist){
        fragmentList = new ArrayList<>();
        fragmentList.add(SimilarArtistFragment.newInstance(artist));
    }

    public void updateData(Artist artist){
        for(BaseTabFragment fragment: fragmentList){
            fragment.updateData(artist);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return fragmentList.get(position).getName();
    }
}