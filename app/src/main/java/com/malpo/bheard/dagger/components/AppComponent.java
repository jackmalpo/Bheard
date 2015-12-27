package com.malpo.bheard.dagger.components;

import com.malpo.bheard.dagger.modules.AppModule;
import com.malpo.bheard.ui.tabs.BaseTabFragment;
import com.malpo.bheard.ui.HomeActivity;
import com.malpo.bheard.ui.SearchFragment;
import com.malpo.bheard.ui.tabs.SimilarArtistFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Jack on 11/25/15.
 * Handles injecting of AppModule into Activites / Fragments
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SearchFragment searchFragment);
    void inject(BaseTabFragment tabFragment);
    void inject(HomeActivity homeActivity);
}
