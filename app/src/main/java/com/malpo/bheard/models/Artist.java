package com.malpo.bheard.models;

import java.util.List;

/**
 * Created by Jack on 10/8/15.
 * LastFm Artist Model
 */
public class Artist {
    private String name;
    private String mbid;
    private String url;
    private List<ArtistImage> image;

    public Artist(){}

    public Artist(String name) {
        this.name = name;
    }

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

    public String getHeaderImageUrl(){
        List<ArtistImage> images = this.getImage();
        String url = "";
        for(ArtistImage image: images){
            if(image.getSize().equals("mega")){
                url = image.getText();
            }
        }
        return url;
    }
}
