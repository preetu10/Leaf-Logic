package com.example.myfirstapp;

public class RatePost {
    private int rating;
    private String feedback;
    private String ratedBy;
    private String ratedAt;

    public RatePost(int rating, String feedback, String ratedAt, String ratedBy) {
        this.rating = rating;
        this.feedback = feedback;
        this.ratedBy = ratedBy;
        this.ratedAt = ratedAt;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRatedBy() {
        return ratedBy;
    }

    public String getRatedAt() {
        return ratedAt;
    }

}
