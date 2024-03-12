package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button getStarted;
    private Button about;

    private Button rating;
    private Button weatherUpdate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted= (Button) findViewById(R.id.startId);
        about=(Button) findViewById(R.id.aboutUs);
        rating=(Button)findViewById(R.id.rating);
        weatherUpdate=(Button)findViewById(R.id.weatherId);
       getStarted.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                  startActivity(intent);

           }
       });
       about.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this,AboutActivity.class);
               startActivity(intent);
           }
       });
       rating.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,AverageRatingActivity.class);
               startActivity(intent);
           }
       });
       weatherUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
               startActivity(intent);
           }
       });





    }
}