package com.example.myfirstapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface FirestoreApiService {
    @POST("ratings")
    Call<Void> addRatingToFirestore( @Body Map<String, Object> ratingMap);
}
