package com.sharvesh.flick.flicker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponses {

    @SerializedName("id")
    private int id_review;

    @SerializedName("results")
    private List<Reviews> reviewsList;

    public int getId_review() {
        return id_review;
    }

    public void setId_review(int id_review) {
        this.id_review = id_review;
    }

    public List<Reviews> getReviewsList() {
        return reviewsList;
    }
}
