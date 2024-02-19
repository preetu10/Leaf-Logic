package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    int year,month,day;
    String[] category={"Student","Businessman","Job Holder","Other"};
    Spinner spinner;
    private Button signUp;
    private EditText edittext1,edittext2,edittext3,edittext4,edittext5;
    EditText dateFormat;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usernames");
        firestore = FirebaseFirestore.getInstance();
        spinner =(Spinner)findViewById(R.id.signupprofessionId);
        dateFormat=(EditText)findViewById(R.id.signupbirthId) ;
        signUp=(Button)findViewById(R.id.signupId);
        edittext1=(EditText) findViewById(R.id.signupNameId);
        edittext2= (EditText)findViewById(R.id.signupEmailId);
        edittext3= (EditText)findViewById(R.id.signupPassId);
        edittext4= (EditText)findViewById(R.id.signupUserNameId);
        edittext5=(EditText) findViewById(R.id.signupPhoneId);
        final Calendar calendar= Calendar.getInstance();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(SignupActivity.this,android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Update the calendar instance with the selected date
                        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        // Set the text in the desired format
                        dateFormat.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        edittext4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Called when the user is typing
                String enteredUserName = charSequence.toString().trim();
                checkUsernameUnique(enteredUserName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               userRegister();
            }


            private void userRegister(){
                String name = edittext1.getText().toString().trim();
                String userName=edittext4.getText().toString().trim();
                String email = edittext2.getText().toString().trim();
                String phone =edittext5.getText().toString().trim();
                String password = edittext3.getText().toString().trim();
                String birthDate=dateFormat.getText().toString().trim();
                String profession=spinner.getSelectedItem().toString().trim();
                if(name.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(userName.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Unique User Name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(birthDate.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Birth Date is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(profession.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Profession is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this,"Email is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignupActivity.this,"Enter a Valid Email Address",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if (phone.isEmpty()) {
                    Toast.makeText(SignupActivity.this,"Phone Number is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(phone.length()<11){
                    Toast.makeText(SignupActivity.this,"Enter Correct Phone Number.",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this,"Password is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
               else if (password.length()<6) {
                    Toast.makeText(SignupActivity.this,"Minimum Length of Password Should Be 6.",Toast.LENGTH_SHORT).show();
                    return;
                }
               else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {

                               if (task.isSuccessful()) {
                                   // Sign up success, save additional user information to Firestore
                                   String userId = mAuth.getCurrentUser().getUid();
                                   FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                   Map<String, Object> user = new HashMap<>();
                                   user.put("name", name);
                                   user.put("userName", userName);
                                   user.put("birthDate", birthDate);
                                   user.put("profession", profession);
                                   user.put("phone", phone);

                                   firestore.collection("users")
                                           .document(userId)
                                           .set(user)
                                           .addOnSuccessListener(aVoid -> {
                                               databaseReference.child(userName).setValue(true);
                                               firebaseUser.sendEmailVerification();
                                               Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(intent);

                                           })
                                           .addOnFailureListener(e ->
                                                   Toast.makeText(SignupActivity.this, "Failed to save user data. " , Toast.LENGTH_SHORT).show()
                                           );
                               } else {
                                   Toast.makeText(SignupActivity.this, "Signup failed. " , Toast.LENGTH_SHORT).show();
                               }
                        }
                           else {
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                }catch(FirebaseAuthUserCollisionException e){
                                    Toast.makeText(SignupActivity.this, "User is already registered. ", Toast.LENGTH_SHORT).show();
                                     Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }catch(Exception e) {
                                    Toast.makeText(SignupActivity.this, "Couldn't register.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    private void checkUsernameUnique(final String enteredUserName) {
        databaseReference.child(enteredUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username is not unique
                    edittext4.setError("Username is already taken. Choose a different one.");
                } else {
                    // Username is unique, remove any previous error
                    edittext4.setError(null);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
                Toast.makeText(SignupActivity.this, "Error checking username uniqueness", Toast.LENGTH_SHORT).show();
            }
        });
    }

}