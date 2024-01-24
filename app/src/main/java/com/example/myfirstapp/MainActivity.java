package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button getStarted;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStarted= (Button) findViewById(R.id.startId);
       getStarted.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                  startActivity(intent);

           }
       });





    }
}