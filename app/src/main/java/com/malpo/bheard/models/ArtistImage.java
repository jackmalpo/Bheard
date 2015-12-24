package com.malpo.bheard.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jack on 10/12/15.
 * LastFm ArtistImage Model
 */
@Parcel(Parcel.Serialization.BEAN)
public class ArtistImage {

    public ArtistImage(){}

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
