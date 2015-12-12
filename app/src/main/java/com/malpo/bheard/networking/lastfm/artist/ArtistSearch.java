package com.malpo.bheard.networking.lastfm.artist;

import com.malpo.bheard.models.Artist;
import com.malpo.bheard.networking.lastfm.LastfmInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Jack on 10/9/15.
 * Handles Retrofit Calls to LastFmInterface
 * @see LastfmInterface
 */
public class ArtistSearch {
    private static final String METHOD = "method";
    private static final String ARTIST = "artist";

    private Map<String, String> queryMap;

    private LastfmInterface lastfmInterface;
    private Retrofit retrofit;

    public ArtistSearch(LastfmInterface lastfmInterface, Retrofit retrofit){
        this.lastfmInterface = lastfmInterface;
        this.retrofit = retrofit;
        queryMap = new HashMap<>();
    }

    public Call<List<Artist>> getSearchResults(String artist) {
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, "artist.search");
        queryMap.put("limit", "5");

        return lastfmInterface.getSearchResults(queryMap);
    }

    public Call<Artist> getArtistCorrection(String artist){
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, "artist.getCorrection");

        return lastfmInterface.getCorrection(queryMap);
    }

    public Call<Artist> getArtistInfo(String artist){
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, "artist.getInfo");
        queryMap.put("autocorrect", "1");

        return lastfmInterface.getInfo(queryMap);
    }

}
