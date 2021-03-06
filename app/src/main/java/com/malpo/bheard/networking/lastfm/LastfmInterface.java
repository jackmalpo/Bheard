package com.malpo.bheard.networking.lastfm;

import com.malpo.bheard.models.Album;
import com.malpo.bheard.models.Artist;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Jack on 10/8/15.
 */
public interface LastfmInterface {

    @GET ("/2.0/")
    Call<Artist> getCorrection(@QueryMap Map<String, String> filters);

    @GET ("/2.0/")
    Call<Artist> getInfo(@QueryMap Map<String, String> filters);

    @GET ("/2.0/")
    Call<List<Artist>> getSearchResults(@QueryMap Map<String, String> filters);

    @GET ("/2.0")
    Call<List<Artist>> getSimilarArtists(@QueryMap Map<String, String> filters);

    @GET ("/2.0")
    Call<List<Album>> getAlbums(@QueryMap Map<String, String> filters);

}
