package com.example.myfirstapp;

import org.tensorflow.lite.support.image.TensorImage;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import android.Manifest;
import android.widget.Toast;

import com.example.myfirstapp.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class DetectionActivity extends AppCompatActivity {
    Button selectBtn, predictBtn, captureBtn;
    TextView result, scientificName, description, properties;
    ImageView imageView;
    Bitmap bitmap;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        getPermission();

        // Load labels
        String[] labels = loadLabels();

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        captureBtn = findViewById(R.id.captureBtn);
        result = findViewById(R.id.result);
        scientificName = findViewById(R.id.scientificName);
        description = findViewById(R.id.plant_description);
        properties = findViewById(R.id.medicinalProperties);
        imageView = findViewById(R.id.imageView);

        // Initialize Firestore
      //  db = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        try {
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Log.e("Firestore", "Firestore initialization failed", e);
        }

        if (firestore != null) {
            Log.d("Firestore", "Firestore initialized successfully");
        } else {
            Log.e("Firestore", "Firestore is null");
        }

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    try {
                        ModelUnquant model = ModelUnquant.newInstance(DetectionActivity.this);

                        // Creating a resized bitmap with 3 channels (RGB)
                        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                        // Convert the bitmap to a tensor image
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        tensorImage.load(bitmap);

                        // Creating the input tensor buffer
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                        inputFeature0.loadBuffer(tensorImage.getBuffer());

                        // Runs model inference and gets result
                        ModelUnquant.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                        int predictedLabelIndex = getMax(outputFeature0.getFloatArray());
                        String predictedLabel = labels[predictedLabelIndex];

                        // Extract the plant name from the label
                        String plantName = extractPlantName(predictedLabel);

                        result.setText(plantName);

                        String indexNumber=extractIndexNumber(predictedLabel);
                        Log.d("plant", plantName);

                        // Fetch data from Firestore
                        fetchPlantData(plantName);

                        // Releases model resources if no longer used
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                } else {
                    Toast.makeText(DetectionActivity.this, "Please select or capture an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    int getMax(float[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[max])
                max = i;
        }
        return max;
    }

    void getPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetectionActivity.this, new String[]{Manifest.permission.CAMERA}, 11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (requestCode == 12) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    void fetchPlantData(String plantName,String label) {
//        Log.d("plant", "Fetching data for plant: " + plantName);
//        firestore.collection("plants")
//                .whereEqualTo("plantName", plantName)
//                .get()
//                .addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//
//                if (document.exists()) {
//                    Log.d("plant found", "Fetched");
//                    String scientificNameText = document.getString("scientificName");
//                    String descriptionText = document.getString("description");
//                    String propertiesText = document.getString("medicinalProperties");
//
//                    scientificName.setVisibility(View.VISIBLE);
//                    description.setVisibility(View.VISIBLE);
//                    properties.setVisibility(View.VISIBLE);
//
//                    scientificName.setText("Scientific Name: " + scientificNameText);
//                    description.setText("Description: " + descriptionText);
//                    properties.setText("Medicinal Properties: " + propertiesText);
//                } else {
//                    Log.d("plant", "No document found for plant: " + label);
//                    Toast.makeText(DetectionActivity.this, "No data found for the detected plant", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Log.d("plant", "Error fetching data for plant: " + label);
//                Toast.makeText(DetectionActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    void fetchPlantData(String plantName) {
        Log.d("Firestore", "Starting fetch for plant: " + plantName);

        // Firestore query using whereEqualTo
        firestore.collection("plants")
                .whereEqualTo("plantName", plantName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0); // Get the first document
                            Log.d("Firestore", "Document found for plant: " + plantName);
                            String scientificNameText = document.getString("scientificName");
                            String descriptionText = document.getString("description");
                            String propertiesText = document.getString("medicinalProperties");

                            scientificName.setVisibility(View.VISIBLE);
                            description.setVisibility(View.VISIBLE);
                            properties.setVisibility(View.VISIBLE);

                            scientificName.setText("Scientific Name: " + scientificNameText);
                            description.setText("Description: " + descriptionText);
                            properties.setText("Medicinal Properties: " + propertiesText);
                        } else {
                            Log.d("Firestore", "No document found for plant: " + plantName);
                            Toast.makeText(DetectionActivity.this, "No data found for the detected plant", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "Error fetching data for plant: " + plantName, task.getException());
                        Toast.makeText(DetectionActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private String[] loadLabels() {
        String[] labels = new String[1001];
        int cnt = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            while (line != null) {
                labels[cnt] = line;
                cnt++;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return labels;
    }

    private String extractPlantName(String label) {
        // Assuming label format is "0 PlantName"
        String[] parts = label.split(" ", 2);
        return parts.length > 1 ? parts[1] : label;
    }

    private String extractIndexNumber(String label) {
        // Assuming label format is "0 PlantName"
        String[] parts = label.split(" ", 2);
        return parts.length > 0 ? parts[0] : "0"; // Default to "0" if no index found
    }
}
