package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    
    private Context context;
    private List<PostModel> postModelList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Map<String, ListenerRegistration> likeListeners;
    private Map<String, ListenerRegistration> dislikeListeners;
    private Map<String, ListenerRegistration> commentListeners;
    public PostsAdapter(Context context){
        this.context=context;
        postModelList=new ArrayList<>();
        likeListeners = new HashMap<>();  // Initializing likeListeners map otherwise it will give nullpointerexception
        dislikeListeners = new HashMap<>();
        commentListeners = new HashMap<>();
    }
    
    public void addPost(PostModel postModel){
        postModelList.add(postModel);
        notifyDataSetChanged();
    }
    public void clearPosts(){
        postModelList.clear();
        notifyDataSetChanged();
    }
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);

        if (postModel.getImagePath() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(postModel.getImagePath()).into(holder.postImage);

        } else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.postContent.setText(postModel.getPostContent());
        holder.postTitle.setText(postModel.getPostTitle());


        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommentsActivity.class);
                intent.putExtra("postId",postModel.getPostId());
                context.startActivity(intent);
            }
        });

        // Load and update likes count
        loadAndListenLikesCount(postModel.getPostId(), holder.likeCount);

        // Load and update dislikes count
        loadAndListenDislikesCount(postModel.getPostId(), holder.disLikeCount);

        // Load and update comments count
        loadAndListenCommentsCount(postModel.getPostId(), holder.commentCount);


        db.collection("likes")
                .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null) {
                            String data = documentSnapshot.getString("postID");
                            if (data != null) {
                                postModel.setLiked(true);
                                holder.likeImage.setImageResource(R.drawable.bluelike);
                            } else {
                                postModel.setLiked(false);
                                holder.likeImage.setImageResource(R.drawable.like);
                            }
                        }else{
                            postModel.setLiked(false);
                            holder.likeImage.setImageResource(R.drawable.like);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        db.collection("dislikes")
                .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null) {
                            String data = documentSnapshot.getString("postID");
                            if (data != null) {
                                postModel.setDisliked(true);
                                holder.dislikeImage.setImageResource(R.drawable.bluedislike);
                            } else {
                                postModel.setDisliked(false);
                                holder.dislikeImage.setImageResource(R.drawable.dislike);
                            }
                        }else{
                            postModel.setDisliked(false);
                            holder.dislikeImage.setImageResource(R.drawable.dislike);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postModel.isLiked()){
                    postModel.setLiked(false);
                    holder.likeImage.setImageResource(R.drawable.like);
                    db.collection("likes")
                            .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                            .delete();
                }
                else{
                    postModel.setLiked(true);
                    holder.likeImage.setImageResource(R.drawable.bluelike);
                    Map<String, Object>  likeDetail = new HashMap<>();
                    likeDetail.put("postID", postModel.getPostId());
                    likeDetail.put("userID", postModel.getUserId());

                    db.collection("likes")
                            .document(postModel.getPostId()+FirebaseAuth.getInstance().getUid())
                            .set(likeDetail);

                    // Remove dislike if previously liked
                    if (postModel.isDisliked()) {
                        postModel.setDisliked(false);
                        holder.dislikeImage.setImageResource(R.drawable.dislike);
                        db.collection("dislikes")
                                .document(postModel.getPostId() + FirebaseAuth.getInstance().getUid())
                                .delete();
                    }
                }
            }
        });


        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postModel.isDisliked()) {
                    postModel.setDisliked(false);
                    holder.dislikeImage.setImageResource(R.drawable.dislike);
                    db.collection("dislikes")
                            .document(postModel.getPostId() + FirebaseAuth.getInstance().getUid())
                            .delete();
                } else {
                    postModel.setDisliked(true);
                    holder.dislikeImage.setImageResource(R.drawable.bluedislike);
                    Map<String, Object> dislikeDetail = new HashMap<>();
                    dislikeDetail.put("postID", postModel.getPostId());
                    dislikeDetail.put("userID", postModel.getUserId());

                    db.collection("dislikes")
                            .document(postModel.getPostId() + FirebaseAuth.getInstance().getUid())
                            .set(dislikeDetail);

                    // Remove like if previously liked
                    if (postModel.isLiked()) {
                        postModel.setLiked(false);
                        holder.likeImage.setImageResource(R.drawable.like);
                        db.collection("likes")
                                .document(postModel.getPostId() + FirebaseAuth.getInstance().getUid())
                                .delete();
                    }
                }
            }
        });



        String uid = postModel.getUserId();
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data read from the document
                    String username = document.getString("userName");
                    if (username != null) {
                        holder.userName.setText(username);
                    }
                } else {

                }
            } else {
                Exception e = task.getException();
            }
        });
    }
    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    private TextView userName, postTitle,postContent,likeCount,disLikeCount,commentCount;
    private ImageView postImage,likeImage,dislikeImage;

    private LinearLayout like,dislike,comments;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        userName=itemView.findViewById(R.id.userName);
        postTitle=itemView.findViewById(R.id.postTitle);
        postContent=itemView.findViewById(R.id.postContent);
        likeCount=itemView.findViewById(R.id.likeCount);
        disLikeCount=itemView.findViewById(R.id.dislikeCount);
        commentCount=itemView.findViewById(R.id.commentCount);
        postImage=itemView.findViewById(R.id.postImage);
        like=itemView.findViewById(R.id.like);
        dislike=itemView.findViewById(R.id.dislike);
        likeImage=itemView.findViewById(R.id.likeImage);
        dislikeImage= itemView.findViewById(R.id.dislikeImage);
        comments=itemView.findViewById(R.id.comments);
    }
}
    private void loadAndListenLikesCount(String postId, TextView textView) {
        likeListeners.put(postId, db.collection("likes")
                .whereEqualTo("postID", postId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Listen error", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int count = queryDocumentSnapshots.size();
                        textView.setText(String.valueOf(count));
                    }
                }));
    }

    private void loadAndListenDislikesCount(String postId, TextView textView) {
        dislikeListeners.put(postId, db.collection("dislikes")
                .whereEqualTo("postID", postId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int count = queryDocumentSnapshots.size();
                        textView.setText(String.valueOf(count));
                    }
                }));
    }

    private void loadAndListenCommentsCount(String postId, TextView textView) {
        commentListeners.put(postId, db.collection("comments")
                .whereEqualTo("postId", postId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int count = queryDocumentSnapshots.size();
                        textView.setText(String.valueOf(count));
                    }
                }));
    }

    public void removeListeners() {
        for (ListenerRegistration listener : likeListeners.values()) {
            listener.remove();
        }
        for (ListenerRegistration listener : dislikeListeners.values()) {
            listener.remove();
        }
        for (ListenerRegistration listener : commentListeners.values()) {
            listener.remove();
        }
    }





}
