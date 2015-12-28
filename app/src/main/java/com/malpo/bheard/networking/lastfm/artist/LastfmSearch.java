package com.malpo.bheard.networking.lastfm.artist;

import com.malpo.bheard.models.Album;
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
public class LastfmSearch {
    private static final String METHOD = "method";
    private static final String ARTIST = "artist";

    private Map<String, String> queryMap;

    private LastfmInterface lastfmInterface;
    private Retrofit retrofit;

    public LastfmSearch(LastfmInterface lastfmInterface, Retrofit retrofit){
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

    public Call<List<Artist>> getSimilarArtists(String artist){
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, "artist.getSimilar");
        queryMap.put("limit", "30");

        return lastfmInterface.getSimilarArtists(queryMap);
    }

    public Call<List<Album>> getAlbums(String artist){
        queryMap.put(ARTIST, artist);
        queryMap.put(METHOD, "artist.getTopAlbums");
        queryMap.put("limit", "50");
        return lastfmInterface.getAlbums(queryMap);
    }

}
