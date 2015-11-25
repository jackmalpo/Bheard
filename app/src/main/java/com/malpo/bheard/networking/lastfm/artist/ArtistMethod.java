package com.malpo.bheard.networking.lastfm.artist;

/**
 * Created by Jack on 10/12/15.
 */
public enum ArtistMethod {

    GET_CORRECTION("artist.getCorrection"),
    GET_INFO("artist.getInfo"),
    GET_SIMILAR("artist.getSimilar"),
    GET_TOP_ALBUMS("artist.getTopAlbums"),
    SEARCH("artist.search");

    private final String name;

    ArtistMethod(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }
}
