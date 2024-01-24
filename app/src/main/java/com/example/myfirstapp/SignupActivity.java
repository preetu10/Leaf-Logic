package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    private Button signUp;
    private EditText edittext1,edittext2,edittext3;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        signUp=(Button)findViewById(R.id.signupId);
        edittext1=(EditText) findViewById(R.id.signupNameId);
        edittext2= (EditText)findViewById(R.id.signupEmailId);
        edittext3= (EditText)findViewById(R.id.signupPassId);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edittext1.getText().toString();
                String email = edittext2.getText().toString();
                String password = edittext3.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this,"Email  is Required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this,"Password  is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}