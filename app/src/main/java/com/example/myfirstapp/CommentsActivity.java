package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfirstapp.databinding.ActivityCommentsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CommentsActivity extends AppCompatActivity {
    ActivityCommentsBinding binding;
    private String postId;
    private CommentAdapter commentAdapter;



    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        commentAdapter=new CommentAdapter(CommentsActivity.this);
        binding.commentsRecyclerView.setAdapter(commentAdapter);
        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
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
        postId=getIntent().getStringExtra("postId");
        loadComments();


        binding.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=binding.commentEd.getText().toString();
                if(comment.trim().length()>0){
                    postComment(comment);
                }
            }
        });
    }
    private void loadComments(){
        firestore.collection("comments")
                .whereEqualTo("postId",postId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        commentAdapter.clearPosts();
                        List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            CommentModel commentModel=ds.toObject(CommentModel.class);
                            if(commentModel!=null) {
                                commentAdapter.addPost(commentModel);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting comments: " + e.getMessage(), e);
                        Toast.makeText(CommentsActivity.this, "Failed to load comments: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void postComment(String comment) {

        String id= UUID.randomUUID().toString();
        CommentModel commentModel=new CommentModel(id,postId, firebaseAuth.getUid(),comment);
        firestore.collection("comments")
                .document(id)
                .set(commentModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CommentsActivity.this, "Comment Added Successfully" , Toast.LENGTH_SHORT).show();
                                binding.commentEd.setText("");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CommentsActivity.this, "Something went wrong" , Toast.LENGTH_SHORT).show();
                                    }
                                });
            commentAdapter.addPost(commentModel);
    }
}