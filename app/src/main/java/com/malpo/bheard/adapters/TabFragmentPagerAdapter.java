package com.malpo.bheard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.malpo.bheard.models.Artist;
import com.malpo.bheard.ui.tabs.AlbumsFragment;
import com.malpo.bheard.ui.tabs.BaseTabFragment;
import com.malpo.bheard.ui.tabs.SimilarArtistFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseTabFragment> fragmentList;

    public TabFragmentPagerAdapter(FragmentManager fm, Artist artist) {
        super(fm);
        initializeFragments(artist);
    }

    private void initializeFragments(Artist artist){
        fragmentList = new ArrayList<>();
        fragmentList.add(AlbumsFragment.newInstance(artist));
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