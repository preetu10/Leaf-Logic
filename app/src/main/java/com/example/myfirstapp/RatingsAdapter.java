//package com.example.myfirstapp;
//
//import android.content.Context;
//import android.media.Rating;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.List;
//
//public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingViewHolder> {
//
//    private Context context;
//    private List<Rating> ratingList;
//
//    public RatingsAdapter(Context context, List<Rating> ratingList) {
//        this.context = context;
//        this.ratingList = ratingList;
//    }
//
//    @NonNull
//    @Override
//    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false);
//        return new RatingViewHolder(view);
//    }
//
//
//    public void addRating(Rating ratingModel){
//        ratingList.add(ratingModel);
//        notifyDataSetChanged();
//    }
//    @Override
//    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
//        Rating rating = ratingList.get(position);
//        holder.feedbackTextView.setText(rating.getFeedback());
//        holder.ratingBar.setRating(rating.getRatingValue());
//    }
//
//    @Override
//    public int getItemCount() {
//        return ratingList.size();
//    }
//
//    public static class RatingViewHolder extends RecyclerView.ViewHolder {
//
//        TextView feedbackTextView;
//        RatingBar ratingBar;
//
//        public RatingViewHolder(@NonNull View itemView) {
//            super(itemView);
//            feedbackTextView = itemView.findViewById(R.id.feedback);
//            ratingBar = itemView.findViewById(R.id.rating_all_rating);
//        }
//    }
//}

package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.R;

import java.util.List;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingViewHolder> {

    private List<Rating> ratings;
    private Context context;

    public RatingsAdapter(Context context, List<Rating> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.feedbackRating.setText(rating.getFeedback());
        holder.ratingValue.setRating((float)rating.getRatingValue());
        holder.ratingValue.setIsIndicator(true);
        holder.user.setText(rating.getRatedBy());

    }


    @Override
    public int getItemCount() {
        return ratings.size();
    }

    static class RatingViewHolder extends RecyclerView.ViewHolder {
       TextView feedbackRating,user;
       RatingBar ratingValue;


        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackRating=itemView.findViewById(R.id.feedback);
            user=itemView.findViewById(R.id.userNameForRating);
            ratingValue=itemView.findViewById(R.id.rating_all_rating);


        }
    }
}

