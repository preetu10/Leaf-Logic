package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {
    private RatingBar rate;
    private EditText feedbackText;

    private Button submitButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

  //  private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/leaf-logic-f474a/databases/(default)/documents/ratings"; // Replace with your Firestore API base URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        rate = findViewById(R.id.ratingBarId);
        feedbackText=findViewById(R.id.feedbackId);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        submitButton=findViewById(R.id.ratingSubmitId);

        rate.setNumStars(5);
        rate.setStepSize(1);
        rate.setRating(0);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) rate.getRating();
                String feedback = feedbackText.getText().toString().trim();
                String ratedBy = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                if (rating == 0) {
                    Toast.makeText(RatingActivity.this, "Please provide rating", Toast.LENGTH_SHORT).show();
                    return;
                } else if (feedback.isEmpty()) {
                    Toast.makeText(RatingActivity.this, "Please provide feedback", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> ratingMap = new HashMap<>();
                ratingMap.put("rating", rating);
                ratingMap.put("feedback", feedback);
                ratingMap.put("ratedBy", ratedBy);
                ratingMap.put("ratedAt", System.currentTimeMillis());
                Intent intent = new Intent(RatingActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               // addRatingToFirestoreApi(ratingMap);
            }
        });
    }

//    private void addRatingToFirestoreApi(Map<String, Object> ratingMap) {
//        FirestoreApiService apiService = RetrofitClient.getClient().create(FirestoreApiService.class);
//
//        Call<Void> call = apiService.addRatingToFirestore( ratingMap);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(RatingActivity.this, "Rating submitted successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RatingActivity.this, ProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(RatingActivity.this, "Failed to submit rating. Please try again.", Toast.LENGTH_SHORT).show();
//                    Log.d("API_RESPONSE", response.toString());
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
//                Toast.makeText(RatingActivity.this, "Failed to submit rating. Please try again.", Toast.LENGTH_SHORT).show();
//
//
//                Intent intent = new Intent(RatingActivity.this, ProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
}
