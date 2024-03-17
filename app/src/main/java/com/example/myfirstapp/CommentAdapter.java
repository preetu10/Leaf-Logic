package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<CommentModel> postModelList;
    public CommentAdapter(Context context){
        this.context=context;
        postModelList=new ArrayList<>();
    }
    
    public void addPost(CommentModel commentModel){
        postModelList.add(commentModel);
        notifyDataSetChanged();
    }
    public void clearPosts(){
        postModelList.clear();
        notifyDataSetChanged();
    }
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommentModel commentModel = postModelList.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.comment.setText(commentModel.getComment());

        String uid = commentModel.getUserId();
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
    private TextView userName, comment;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        userName=itemView.findViewById(R.id.userName);
        comment=itemView.findViewById(R.id.comment);

    }
}
}
