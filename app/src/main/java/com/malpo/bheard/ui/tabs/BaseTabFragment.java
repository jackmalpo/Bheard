package com.malpo.bheard.ui.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.malpo.bheard.MyApplication;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.LastfmSearch;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by Jack on 12/22/15.
 */
public abstract class BaseTabFragment extends Fragment {

    @Inject
    LastfmSearch search;
    @Inject EventBus bus;

    public abstract CharSequence getName();
    public abstract void updateData(Artist artist);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
    }
}
