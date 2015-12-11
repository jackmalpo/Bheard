package com.malpo.bheard.eventbus;

import com.malpo.bheard.models.Artist;

/**
 * Created by Jack on 11/28/15.
 * Event emmitted on new artist found.
 * @see com.malpo.bheard.ui.SearchFragment
 */
public class SearchResultEvent {
    public final Artist artist;

    public SearchResultEvent(Artist artist) {
        this.artist = artist;
    }
}
