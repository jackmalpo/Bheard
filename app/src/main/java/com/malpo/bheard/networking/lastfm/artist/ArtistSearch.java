package com.malpo.bheard.networking.lastfm.artist;

import com.malpo.bheard.models.Artist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Jack on 10/9/15.
 */
public class ArtistSearch {
    private static final String METHOD = "method";
    private static final String ARTIST = "artist";

    private ArtistInterface apiService;
    private Map<String, String> queryMap;
    private String artist;

    public ArtistSearch(String artist){
        this.artist = artist;
    }

    private void setupRetrofit(ArtistMethod method){
        Retrofit retrofit = new ArtistRetrofitFactory().createService(method);
        apiService = retrofit.create(ArtistInterface.class);
        queryMap = new HashMap<>();
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, method.getName());
    }

    public Call<List<Artist>> getSearchResults() {
        setupRetrofit(ArtistMethod.SEARCH);
        queryMap.put("limit", "10");
        return apiService.getSearchResults(queryMap);
    }

    public Call<Artist> getCorrection(){
        setupRetrofit(ArtistMethod.GET_CORRECTION);
        return apiService.getCorrection(queryMap);
    }

    public Call<Artist> getInfo(){
        setupRetrofit(ArtistMethod.GET_INFO);
        queryMap.put("autocorrect", "1");
        return apiService.getInfo(queryMap);
    }



}
