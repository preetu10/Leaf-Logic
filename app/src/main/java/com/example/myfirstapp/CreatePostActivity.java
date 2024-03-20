package com.example.myfirstapp;

import static java.lang.System.exit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.databinding.ActivityCreatePostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Uri pickedImaageUri;

    Timestamp currentTimeStamp = Timestamp.now();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Date currentDate = currentTimeStamp.toDate();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        DocumentReference userRef = firestore.collection("users").document(userID);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data read from the document
                    String username = document.getString("userName");
                    Log.d("test",username);
                    if (username != null) {
                        binding.userName.setText(username);
                    }
                    else{
                        Toast.makeText(CreatePostActivity.this,"userName cannot be loaded", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(CreatePostActivity.this,"userName cannot be loadeddd", Toast.LENGTH_SHORT).show();
                }
            } else {
                Exception e = task.getException();
                Toast.makeText(CreatePostActivity.this,"userName cannot be loadeddddddddddddd", Toast.LENGTH_SHORT).show();

            }
        });



        binding.pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void openGallery(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/+");
        startActivityForResult(intent,100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.create_post_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int itemId=item.getItemId();
       if(itemId==R.id.post){
           ProgressDialog progressDialog=new ProgressDialog(CreatePostActivity.this);
           progressDialog.setTitle("Loading...");
           progressDialog.show();

           String id= UUID.randomUUID().toString();
           String userID;
           FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
           userID = currentUser.getUid();



           String title=binding.postTitle.getText().toString().trim();
           String postContent=binding.postDescription.getText().toString().trim();
           if(title.isEmpty()){
               Toast.makeText(CreatePostActivity.this, "Post title is required", Toast.LENGTH_SHORT).show();
              exit(-1);
           }
           if(postContent.isEmpty()){
               Toast.makeText(CreatePostActivity.this, "Post Content is required", Toast.LENGTH_SHORT).show();
             exit(-1 );
           }

           StorageReference storageReference = FirebaseStorage.getInstance().getReference("Posts").child(id + ".png");


           if(pickedImaageUri!=null){
               storageReference.putFile(pickedImaageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       storageReference.getDownloadUrl()
                               .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       progressDialog.cancel();
                                       PostModel postModel= new PostModel(id,userID,title,postContent,uri.toString(),currentTimeStamp);
                                       firestore.collection("posts")
                                               .document(id)
                                               .set(postModel)
                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       // Post added successfully
                                                       Toast.makeText(CreatePostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                                                       Intent intent = new Intent(CreatePostActivity.this, NewsFeedActivity.class);
                                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                       startActivity(intent);
                                                       finish(); // Optionally finish the activity after posting
                                                   }
                                               })
                                               .addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       // Handle any errors
                                                       Toast.makeText(CreatePostActivity.this, "Failed to create post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                       progressDialog.cancel();
                                                   }
                                               });
                                   }
                               });

                   }
               });
           }
           else{
               PostModel postModel= new PostModel(id,userID,title,postContent,null,currentTimeStamp);
               firestore.collection("posts")
                       .document(id)
                       .set(postModel)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               // Post added successfully
                               Toast.makeText(CreatePostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(CreatePostActivity.this, NewsFeedActivity.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                               finish(); // Optionally finish the activity after posting
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               // Handle any errors
                               Toast.makeText(CreatePostActivity.this, "Failed to create post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                               progressDialog.cancel();
                           }
                       });
               progressDialog.cancel();
           }
           return true;
       }
       return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(data!=null){
                pickedImaageUri=data.getData();
                binding.pickedImage.setImageURI(pickedImaageUri);
                Glide.with(CreatePostActivity.this)
                        .load(pickedImaageUri).into(binding.pickedImage);
            }
            else{
                Toast.makeText(CreatePostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}