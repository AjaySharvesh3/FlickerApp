package com.sharvesh.flick.flicker.model;

import com.google.gson.annotations.SerializedName;

public class Casts {

    @SerializedName("name")
    private String cast_name;

    @SerializedName("character")
    private String cast_character;

    @SerializedName("profile_path")
    private String cast_poster;

    @SerializedName("id")
    private Integer id;

    public Casts(String cast_name, String cast_character, String cast_poster, Integer id) {
        this.cast_name = cast_name;
        this.cast_character = cast_character;
        this.cast_poster = cast_poster;
        this.id = id;
    }

    private String imageBaseUrl = "http://image.tmdb.org/t/p/w185";

    public String getCast_name() {
        return cast_name;
    }

    public void setCast_name(String cast_name) {
        this.cast_name = cast_name;
    }

    public String getCast_character() {
        return cast_character;
    }

    public void setCast_character(String cast_character) {
        this.cast_character = cast_character;
    }

    public String getCast_poster() {
        return "http://image.tmdb.org/t/p/w185" + cast_poster;
    }

    public void setCast_poster(String cast_poster) {
        this.cast_poster = cast_poster;
    }

    public Integer getId() {
        return id;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
