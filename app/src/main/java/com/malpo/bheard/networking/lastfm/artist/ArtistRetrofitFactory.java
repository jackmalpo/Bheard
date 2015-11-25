package com.malpo.bheard.networking.lastfm.artist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.artist.json.ArtistCorrectionDeserializer;
import com.malpo.bheard.networking.lastfm.artist.json.ArtistDeserializer;
import com.malpo.bheard.networking.lastfm.artist.json.ArtistSearchDeserializer;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Jack on 10/8/15.
 */
public class ArtistRetrofitFactory {

    private static final String BASE_URL = "http://ws.audioscrobbler.com";
    private static final String API_KEY = "&api_key=9f6527a795ad3e1f3a73aa3bf2d5a428";
    private static final String FORMAT = "&format=json";

    private OkHttpClient client;

    public ArtistRetrofitFactory(){}

    public Retrofit createService(ArtistMethod method){

        client = new OkHttpClient();
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

        Retrofit retrofit = createRetrofit(method);

        return retrofit;
    }

    private Retrofit createRetrofit(ArtistMethod method){
        switch (method){
            case GET_CORRECTION:
                return getCorrection();
            case GET_INFO:
                return getInfo();
            case GET_SIMILAR:
                return getSimilar();
            case GET_TOP_ALBUMS:
                return getTopAlbums();
            case SEARCH:
                return searchResults();
        }
        return null;
    }

    private Retrofit getCorrection(){
        Gson gson= new GsonBuilder().registerTypeAdapter(Artist.class, new ArtistCorrectionDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    private Retrofit searchResults(){
        Gson gson= new GsonBuilder().registerTypeAdapter(Artist.class, new ArtistSearchDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    private Retrofit getInfo(){
        return defaultRetrofit();
    }

    private Retrofit getSimilar(){
        return defaultRetrofit();
    }
    private Retrofit getTopAlbums(){
        return defaultRetrofit();
    }

    private Retrofit defaultRetrofit(){
        Gson gson= new GsonBuilder().registerTypeAdapter(Artist.class, new ArtistDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

}
