package com.example.myfirstapp;

public class Rating {
    private String feedback;
    private String ratedBy;
    private float ratingValue;

    public Rating() {
        // No-argument constructor needed for Firestore serialization
    }

    public String getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(String ratedBy) {
        this.ratedBy = ratedBy;
    }

    public Rating(String feedback, float ratingValue,String ratedBy) {
        this.feedback = feedback;
        this.ratingValue = ratingValue;
        this.ratedBy=ratedBy;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }
}
