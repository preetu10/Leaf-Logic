package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.FieldIndex;

import org.w3c.dom.Text;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private Button signout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private TextView userText,nameText,emailText,phoneText,birthText,professionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        signout=(Button) findViewById(R.id.signout);
        userText=(TextView)findViewById(R.id.user_name) ;
        nameText=(TextView) findViewById(R.id.u_name);
        emailText=(TextView)findViewById(R.id.u_email) ;
        phoneText=(TextView)findViewById(R.id.u_phone);
        birthText=(TextView) findViewById(R.id.u_birth);
        professionText=(TextView)findViewById(R.id.u_profession) ;

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = currentUser.getEmail();

                                String phone=document.getString("phone");
                                String profession = document.getString("profession");
                                String userName= document.getString("userName");
                                String birthDate= document.getString("birthDate");

                                userText.setText(userName);
                                emailText.setText(email);
                                nameText.setText(name);
                                phoneText.setText(phone);
                                professionText.setText(profession);
                                birthText.setText(birthDate);

                                sessionManager.saveUserInfo(userName,email,name,birthDate,profession,phone);
                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"No Information Found",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this,"An error occurred.",Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In your MainActivity or any other activity where you want to implement logout
                if(sessionManager.isLoggedIn()) {
                    FirebaseAuth.getInstance().signOut();
                    sessionManager.setLoggedIn(false);
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}