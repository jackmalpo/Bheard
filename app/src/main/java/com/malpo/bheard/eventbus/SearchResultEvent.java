package com.malpo.bheard.eventbus;

import com.malpo.bheard.models.Artist;

/**
 * Created by Jack on 11/28/15.
 */
public class SearchResultEvent {
    public final Artist artist;

    public SearchResultEvent(Artist artist) {
        this.artist = artist;
    }
}
