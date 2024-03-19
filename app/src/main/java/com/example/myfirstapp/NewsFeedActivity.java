package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myfirstapp.databinding.ActivityNewsFeedBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {
    ActivityNewsFeedBinding binding;

    private PostsAdapter postsAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        postsAdapter=new PostsAdapter(NewsFeedActivity.this);
        binding.postRecyclerView.setAdapter(postsAdapter);
        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Log.e("Firestore", "Firestore initialization failed", e);
        }

        if (firestore != null) {
            Log.d("Firestore", "Firestore initialized successfully");
        } else {
            Log.e("Firestore", "Firestore is null");
        }
        loadPosts();

        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this,CreatePostActivity.class));
            }
        });
    }
    private void loadPosts(){
        firestore.collection("posts")
                .orderBy("postingTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        postsAdapter.clearPosts();
                        List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds: dsList){
                            PostModel postModel = ds.toObject(PostModel.class);
                            if(postModel!=null){
                                postsAdapter.addPost(postModel);
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewsFeedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}