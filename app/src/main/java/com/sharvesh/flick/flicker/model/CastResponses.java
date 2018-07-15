package com.sharvesh.flick.flicker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponses {

    @SerializedName("id")
    private int id;

    @SerializedName("cast")
    private List<Casts> results;

    public int getIdCast() {
        return id;
    }

    public void setIdCast(int id) {
        this.id = id;
    }

    public List<Casts> getResults() {
        return results;
    }

}
