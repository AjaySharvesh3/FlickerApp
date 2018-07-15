package com.sharvesh.flick.flicker.model;

import com.google.gson.annotations.SerializedName;

public class Trailers {

    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    public Trailers(String key, String name){
        this.key = key;
        this.name = name;
    }

    private String baseImageUrl = "https://img.youtube.com/";

    public String getPosterPath() {
        return "https://img.youtube.com/" + key;
    }

    public String getKey(){
        return "https://img.youtube.com/" + key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }
}
