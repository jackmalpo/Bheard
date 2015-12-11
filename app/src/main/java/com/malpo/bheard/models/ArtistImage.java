package com.malpo.bheard.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jack on 10/12/15.
 * LastFm ArtistImage Model
 */
public class ArtistImage {

    @SerializedName("#text")
    private String text;

    private String size;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
