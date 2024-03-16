package com.example.myfirstapp;

public class PostModel {

    private String postId,userId,postContent,postTitle,imagePath;

    public PostModel(String postId, String userId, String postTitle,String postContent,  String imagePath, long postingTime) {
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

    public long getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(long postingTime) {
        this.postingTime = postingTime;
    }

    private long postingTime;
}
