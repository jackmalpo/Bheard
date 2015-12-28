package com.malpo.bheard.dagger.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.malpo.bheard.MyApplication;
import com.malpo.bheard.models.Album;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.LastfmInterface;
import com.malpo.bheard.networking.lastfm.artist.LastfmSearch;
import com.malpo.bheard.networking.lastfm.artist.json.AlbumListDeserializer;
import com.malpo.bheard.networking.lastfm.artist.json.ArtistDeserializer;
import com.malpo.bheard.networking.lastfm.artist.json.ArtistListDeserializer;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Jack on 11/25/15.
 * Dagger Module construction singletons that will be provided throughout the app.
 */
@Module
public class AppModule {

    private final MyApplication mApplication;
    private static final String BASE_URL = "http://ws.audioscrobbler.com";
    private static final String API_KEY = "&api_key=9f6527a795ad3e1f3a73aa3bf2d5a428";
    private static final String FORMAT = "&format=json";

    public AppModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesApplicationContext(){
        return mApplication;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(){
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .url(original.url() + API_KEY + FORMAT)
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        });
        return client;
    }


    @Provides
    @Singleton
    Gson providesGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Artist.class, new ArtistDeserializer())
                .registerTypeAdapter(new TypeToken<List<Artist>>(){}.getType(), new ArtistListDeserializer())
                .registerTypeAdapter(new TypeToken<List<Album>>(){}.getType(), new AlbumListDeserializer())
                .create();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    LastfmInterface providesLastfmInterface(Retrofit retrofit) {
        return retrofit.create(LastfmInterface.class);
    }

    @Provides
    @Singleton
    LastfmSearch providesArtistSearch(LastfmInterface lastfmInterface, Retrofit retrofit){
        return new LastfmSearch(lastfmInterface, retrofit);
    }

    @Provides
    @Singleton
    EventBus providesEventBus(){
        return EventBus.getDefault();
    }
}

