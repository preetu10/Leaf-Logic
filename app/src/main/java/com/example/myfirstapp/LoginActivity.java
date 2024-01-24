package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button signup,signin;
    private EditText edittext1,edittext2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup= (Button) findViewById(R.id.signup);
        signin= (Button) findViewById(R.id.login);
        edittext1=(EditText) findViewById(R.id.loginEmailId);
        edittext2= (EditText)findViewById(R.id.loginPassId);




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittext1.getText().toString();
                String password = edittext2.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this,"Email is Required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,"Password is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =new Intent(LoginActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


    }
}