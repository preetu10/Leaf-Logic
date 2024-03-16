package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myfirstapp.databinding.ActivityNewsFeedBinding;

public class NewsFeedActivity extends AppCompatActivity {
    ActivityNewsFeedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this,CreatePostActivity.class));
            }
        });
    }
}