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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewsFeedActivity extends AppCompatActivity {
    ActivityNewsFeedBinding binding;

    private PostsAdapter postsAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DocumentSnapshot firstVisiblePost;
    private DocumentSnapshot lastVisiblePost;
    private int pageSize = 3; // Number of posts per page
    private int currentPage = 1; // Current page number

    private int totalPosts=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postsAdapter = new PostsAdapter(NewsFeedActivity.this);
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

        binding.next.setOnClickListener(view -> loadNextPage());
        binding.previous.setOnClickListener(view -> loadPreviousPage());

        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this, CreatePostActivity.class));
            }
        });

        // Load the first page initially
        loadPage(currentPage);
    }

    private void loadPage(int page) {
        currentPage = page;
        if (page == 1) {
            binding.previous.setEnabled(false);
        } else {
            binding.previous.setEnabled(true);
        }

        firestore.collection("posts")
                .orderBy("postingTime", Query.Direction.DESCENDING)
                .limit(pageSize)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots != null) {
                            List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                            if (!dsList.isEmpty()) {
                                firstVisiblePost = dsList.get(0);
                                lastVisiblePost = dsList.get(dsList.size() - 1);
                            }
                            postsAdapter.clearPosts();
                            for (DocumentSnapshot ds : dsList) {
                                PostModel postModel = ds.toObject(PostModel.class);
                                if (postModel != null) {
                                    postsAdapter.addPost(postModel);
                                }
                            }
                        }

                        // Check if there are less than pageSize posts, disable the Next button
                        if (task.getResult().size() < pageSize) {
                            binding.next.setEnabled(false);
                        } else {
                            binding.next.setEnabled(true);
                        }
                    } else {
                        Log.e("NewsFeedActivity", "Error getting documents: ", task.getException());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewsFeedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadNextPage() {
        currentPage++;
        Log.d("next",String.valueOf(currentPage));

        // Get the total pages using the getTotalPages() method
        getTotalPages().addOnSuccessListener(totalPages -> {
            Log.d("check total", String.valueOf(totalPages));

            // Compare currentPage with totalPages to determine if Next button should be enabled
            if (currentPage == totalPages) {
                Log.d("check total", String.valueOf(totalPages));
                binding.next.setEnabled(false);
            } else {
                binding.next.setEnabled(true);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that may occur when getting total pages
            Log.e("NewsFeedActivity", "Error getting total pages: ", e);
        });

        binding.previous.setEnabled(true);

        if (lastVisiblePost != null) {
            firestore.collection("posts")
                    .orderBy("postingTime", Query.Direction.DESCENDING)
                    .startAfter(lastVisiblePost)
                    .limit(pageSize)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (queryDocumentSnapshots != null) {
                                List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                                if (!dsList.isEmpty()) {
                                    firstVisiblePost = dsList.get(0);
                                    lastVisiblePost = dsList.get(dsList.size() - 1);
                                }
                                postsAdapter.clearPosts();
                                for (DocumentSnapshot ds : dsList) {
                                    PostModel postModel = ds.toObject(PostModel.class);
                                    if (postModel != null) {
                                        postsAdapter.addPost(postModel);
                                    }
                                }
                            }
                        } else {
                            Log.e("NewsFeedActivity", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Toast.makeText(this, "No more posts to load", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadPreviousPage() {
        currentPage--;
        Log.d("previous",String.valueOf(currentPage));
        if (currentPage == 1) {
            binding.previous.setEnabled(false);
        } else {
            binding.previous.setEnabled(true);
        }
        binding.next.setEnabled(true);

        if (firstVisiblePost != null) {
            firestore.collection("posts")
                    .orderBy("postingTime", Query.Direction.DESCENDING)
                    .endBefore(firstVisiblePost)
                    .limitToLast(pageSize)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            if (queryDocumentSnapshots != null) {
                                List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                                if (!dsList.isEmpty()) {
                                    firstVisiblePost = dsList.get(0);
                                    lastVisiblePost = dsList.get(dsList.size() - 1);
                                }
                                postsAdapter.clearPosts();
                                for (DocumentSnapshot ds : dsList) {
                                    PostModel postModel = ds.toObject(PostModel.class);
                                    if (postModel != null) {
                                        postsAdapter.addPost(postModel);
                                    }
                                }
                            }
                        } else {
                            Log.e("NewsFeedActivity", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Toast.makeText(this, "No previous posts to load", Toast.LENGTH_SHORT).show();
        }
    }

    private Task<Integer> getTotalPosts() {
        // Query the "posts" collection
        return firestore.collection("posts")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        // Get the number of documents in the result
                        return task.getResult().size();
                    } else {
                        // Handle any errors that may occur
                        Exception e = task.getException();
                        Log.e("NewsFeedActivity", "Error getting total posts: ", e);
                        return 0;
                    }
                });
    }

    private Task<Integer> getTotalPages() {
        return getTotalPosts().continueWith(totalPostsTask -> {
            if (totalPostsTask.isSuccessful()) {
                int totalPosts = totalPostsTask.getResult();
                Log.d("from get posts number from pages function", String.valueOf(totalPosts));
                int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
                Log.d("from get pages function", String.valueOf(totalPages));
                return totalPages;
            } else {
                // Handle any errors that may occur
                Exception e = totalPostsTask.getException();
                Log.e("NewsFeedActivity", "Error getting total posts: ", e);
                return 0;
            }
        });
    }




}
