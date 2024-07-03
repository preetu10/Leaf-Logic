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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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
    Toolbar toolbar;
    SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ratings);

        recyclerView = findViewById(R.id.recycler_rating);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ratingsAdapter = new RatingsAdapter(this, ratingList);
        recyclerView.setAdapter(ratingsAdapter);

        db = FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());

        fetchRatings();
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
            Intent intent = new Intent(AllRatingsActivity.this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(id==R.id.logout_item){
            if(sessionManager.isLoggedIn()) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.setLoggedIn(false);
                Intent intent = new Intent(AllRatingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
        return true;
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
