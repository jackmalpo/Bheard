package com.malpo.bheard;

import android.app.Application;
import com.malpo.bheard.dagger.components.AppComponent;
import com.malpo.bheard.dagger.modules.AppModule;
import com.malpo.bheard.dagger.components.DaggerAppComponent;

/**
 * Created by Jack on 11/27/15.
 */
public class MyApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getComponent(){
        return component;
    }

}
