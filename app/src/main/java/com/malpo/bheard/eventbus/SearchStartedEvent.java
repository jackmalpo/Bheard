package com.malpo.bheard.eventbus;

import com.malpo.bheard.models.Artist;

/**
 * Created by Jack on 11/28/15.
 * Event emmitted on new artist search STARTED
 * @see com.malpo.bheard.ui.SearchFragment
 */
public class SearchStartedEvent {
    public final String artistName;
    public final Artist artist;

    public SearchStartedEvent(String artistName) {
        this.artistName = artistName;
        this.artist = null;
    }

    public SearchStartedEvent(Artist artist){
        this.artist = artist;
        this.artistName = "";
    }
}
