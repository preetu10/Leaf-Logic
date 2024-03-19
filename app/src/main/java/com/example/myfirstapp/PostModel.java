package com.example.myfirstapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostModel {

    private String postId,userId,postContent,postTitle,imagePath;
    private Timestamp postingTime;

    private boolean isLiked;
    private boolean isDisliked;


    public PostModel() {
        // Default constructor required for Firestore deserialization
    }


    public PostModel(String postId, String userId, String postTitle,String postContent,  String imagePath, Timestamp postingTime) {
        this.postId = postId;
        this.userId = userId;
        this.postContent = postContent;
        this.postTitle = postTitle;
        this.imagePath = imagePath;
        this.postingTime = postingTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Timestamp getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(Timestamp postingTime) {
        this.postingTime = postingTime;
    }


    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }
}
