package com.malpo.bheard.dagger.components;

import com.malpo.bheard.dagger.modules.AppModule;
import com.malpo.bheard.ui.HomeActivity;
import com.malpo.bheard.ui.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Jack on 11/25/15.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(SearchFragment searchFragment);
    void inject(HomeActivity homeActivity);
}
