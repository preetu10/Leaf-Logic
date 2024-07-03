package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AverageRatingActivity extends AppCompatActivity {
    private Button doRating,seeAllRating;
    private TextView averageRating, usersCount;

    private RatingBar rate, averageRatingBar;
    private EditText feedbackText;

    private Button submitButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private ListenerRegistration averageRatingListener;
    Toolbar toolbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_rating);
        doRating=(Button)findViewById(R.id.doRatingId);
        averageRating=(TextView) findViewById(R.id.averageRatingId);
        usersCount=(TextView) findViewById(R.id.userCountId);
        averageRatingBar=(RatingBar) findViewById(R.id.averageRatingBarId);
        sessionManager = new SessionManager(getApplicationContext());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        seeAllRating=(Button) findViewById(R.id.seeAllRatingsId);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(currentUser!=null){
            doRating.setVisibility(View.VISIBLE);
        }
        else{
            doRating.setVisibility(View.GONE);
        }

        seeAllRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AverageRatingActivity.this, AllRatingsActivity.class);
                startActivity(intent);
            }
        });

        doRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(AverageRatingActivity.this);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));
                dialog.setContentView(R.layout.activity_rating);
                dialog.show();
                rate = dialog.findViewById(R.id.ratingBarId);
                feedbackText=dialog.findViewById(R.id.feedbackId);
                submitButton=dialog.findViewById(R.id.ratingSubmitId);
                rate.setNumStars(5);
                rate.setStepSize(1);
                rate.setRating(0);


                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int ratingCount = (int) rate.getRating();
                        String feedback = feedbackText.getText().toString().trim();
                        String ratedById = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                        if (ratingCount  == 0) {
                            Toast.makeText(AverageRatingActivity.this, "Please provide rating", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (feedback.isEmpty()) {
                            Toast.makeText(AverageRatingActivity.this, "Please provide feedback", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            firestore.collection("users")
                                    .document(ratedById)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            String ratedBy = document.getString("userName");
                                            checkAndSubmitRating(ratedById,ratingCount,feedback,ratedBy);
                                            dialog.dismiss();

                                        } else {
                                            Log.e("Firestore", "Error fetching data for plant: ");
                                            Toast.makeText(AverageRatingActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });
            }
        });
        // Listen for changes in real time to the average rating
        listenForAverageRating();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.dashboard_item){
            Intent intent = new Intent(AverageRatingActivity.this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(id==R.id.logout_item){
            if(sessionManager.isLoggedIn()) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.setLoggedIn(false);
                Intent intent = new Intent(AverageRatingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove listeners when the activity is destroyed to avoid memory leakage
        if (averageRatingListener != null) {
            averageRatingListener.remove();
        }
    }
    private void listenForAverageRating() {
        Query query = firestore.collection("ratings");
        averageRatingListener = query.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Firestore", "Listen error", error);
                    return;
                }

                float totalRating = 0;
                int totalUsers = 0;

                assert snapshots != null;
                for (DocumentSnapshot document : snapshots) {
                    if (document.contains("ratingValue")) {
                        totalRating += Objects.requireNonNull(document.getDouble("ratingValue"));
                        totalUsers++;
                    }
                }
                usersCount.setText(String.valueOf(totalUsers));
                if (totalUsers > 0) {
                    float average = totalRating / totalUsers;

                    averageRating.setText(String.format("%.1f", average));
                    averageRatingBar.setRating((float)average);
                    averageRatingBar.setIsIndicator(true);
                } else {
                    averageRating.setText("0.00");
                    averageRatingBar.setRating(0);
                    averageRatingBar.setIsIndicator(true);

                }
            }
        });
    }

    private void checkAndSubmitRating(String userId, int ratingCount, String feedback,String ratedBy) {
        firestore.collection("ratings")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User has already submitted a rating
                            updateRating(userId, ratingCount, feedback,ratedBy);
                        } else {
                            // User hasn't submitted a rating before
                            submitNewRating(userId, ratingCount, feedback,ratedBy);
                        }
                    } else {
                        Log.e("Firestore", "Error checking for existing rating", task.getException());
                    }
                });
    }

    private void updateRating(String userId, int ratingCount, String feedback,String ratedBy) {
        Map<String, Object> rating = new HashMap<>();
        rating.put("ratingValue", ratingCount);
        rating.put("feedback", feedback);
        rating.put("ratedBy",ratedBy);

        firestore.collection("ratings")
                .document(userId)
                .update(rating)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AverageRatingActivity.this, "Rating updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating rating", e);
                    Toast.makeText(AverageRatingActivity.this, "Failed to update rating. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
    private void submitNewRating(String userId, int ratingCount, String feedback,String ratedBy) {
        Map<String, Object> rating = new HashMap<>();
        rating.put("ratingValue", ratingCount);
        rating.put("feedback", feedback);
        rating.put("ratedBy",ratedBy);


        firestore.collection("ratings")
                .document(userId)
                .set(rating)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AverageRatingActivity.this, "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error submitting new rating", e);
                    Toast.makeText(AverageRatingActivity.this, "Failed to submit rating. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}