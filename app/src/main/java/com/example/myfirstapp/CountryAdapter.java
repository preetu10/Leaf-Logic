//package com.example.myfirstapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.myfirstapp.R;
//import com.example.myfirstapp.CountriesQuery;
//import java.util.List;
//
//public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
//
//    private List<CountriesQuery.Country> countries;
//    private Context context;
//    private OnItemClickListener onItemClickListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(CountriesQuery.Country country);
//    }
//
//    public CountryAdapter(Context context, List<CountriesQuery.Country> countries, OnItemClickListener onItemClickListener) {
//        this.context = context;
//        this.countries = countries;
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    @NonNull
//    @Override
//    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
//        return new CountryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
//        CountriesQuery.Country country = countries.get(position);
//        holder.bind(country, onItemClickListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return countries.size();
//    }
//
//    static class CountryViewHolder extends RecyclerView.ViewHolder {
//
//        TextView countryNameTextView;
//        TextView countryEmojiTextView;
//        TextView countryCapitalTextView;
//
//        public CountryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            countryNameTextView = itemView.findViewById(R.id.countryNameTextView);
//            countryEmojiTextView = itemView.findViewById(R.id.countryEmojiTextView);
//            countryCapitalTextView = itemView.findViewById(R.id.countryCapitalTextView);
//        }
//
//        public void bind(CountriesQuery.Country country, OnItemClickListener onItemClickListener) {
//            countryNameTextView.setText(country.name());
//            countryEmojiTextView.setText(country.emoji());
//            countryCapitalTextView.setText(country.capital());
//
//            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(country));
//        }
//    }
//}
//
package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myfirstapp.CountriesQuery;
import com.example.myfirstapp.R;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CountriesQuery.Country> countries;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(CountriesQuery.Country country);
    }

    public CountryAdapter(Context context, List<CountriesQuery.Country> countries, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.countries = countries;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountriesQuery.Country country = countries.get(position);
        holder.countryNameTextView.setText(country.name());
      //  holder.countryEmojiTextView.setText(country.emoji());
        holder.countryCapitalTextView.setText(country.capital());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(country));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView countryNameTextView;
       // TextView countryEmojiTextView;
        TextView countryCapitalTextView;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            countryNameTextView = itemView.findViewById(R.id.countryNameTextView);
            //countryEmojiTextView = itemView.findViewById(R.id.countryEmojiTextView);
            countryCapitalTextView = itemView.findViewById(R.id.countryCapitalTextView);
        }
    }
}

