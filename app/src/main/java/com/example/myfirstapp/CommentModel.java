package com.example.myfirstapp;

public class CommentModel {
    private String id,postId,userId,comment;



    public CommentModel(String commentId, String postId, String userId, String comment) {
        this.id = commentId;
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;

    }
    public CommentModel(){

    }
    public String getCommentId() {
        return id;
    }

    public void setCommentId(String commentId) {
        this.id = commentId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }






}
