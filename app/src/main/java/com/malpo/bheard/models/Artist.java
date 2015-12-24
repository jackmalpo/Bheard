package com.malpo.bheard.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Jack on 10/8/15.
 * LastFm Artist Model
 */
@Parcel(Parcel.Serialization.BEAN)
public class Artist {
    private String name;
    private String mbid;
    private String url;
    private List<ArtistImage> image;

    public Artist(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ArtistImage> getImage() {
        return image;
    }

    public void setImage(List<ArtistImage> image) {
        this.image = image;
    }

    public String getListImageUrl(){
        return getImageUrl("large");
    }

    public String getHeaderImageUrl(){
        return getImageUrl("mega");
    }

    private String getImageUrl(String which){
        List<ArtistImage> images = this.getImage();
        String url = "";
        for(ArtistImage image: images){
            if(image.getSize().equals(which)){
                url = image.getText();
            }
        }
        return url;
    }
}
