//package com.example.myfirstapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//
//public class AllRatingsActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_ratings);
//
//    }
//}

package com.example.myfirstapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllRatingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RatingsAdapter ratingsAdapter;
    private List<Rating> ratingList=new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ratings);

        recyclerView = findViewById(R.id.recycler_rating);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ratingsAdapter = new RatingsAdapter(this, ratingList);
        recyclerView.setAdapter(ratingsAdapter);

        db = FirebaseFirestore.getInstance();

        fetchRatings();
    }

    private void fetchRatings() {
        db.collection("ratings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots != null) {
                            List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot ds : dsList) {
                               Rating ratingModel = ds.toObject(Rating.class);
                                if (ratingModel != null) {
                                    Log.d("AllRatingsActivity", "Rating: " + ratingModel.getRatedBy());
                                    ratingList.add(ratingModel);  // Add the rating to the list
                                }
                            }
                            ratingsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("AllRatingActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}
