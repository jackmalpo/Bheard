package com.malpo.bheard.models;

import java.util.List;

/**
 * Created by Jack on 12/28/15.
 */
public class Album {

    private String name;
    private List<Image> image;

    public Album(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public String getListImageUrl(){
        return getImageUrl("large");
    }

    private String getImageUrl(String which){
        List<Image> images = this.getImage();
        String url = "";
        for(Image image: images){
            if(image.getSize().equals(which)){
                url = image.getText();
            }
        }
        return url;
    }
}
