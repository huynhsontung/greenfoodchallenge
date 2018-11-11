package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddGreenMealActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;
    ImageView imageToUpload, mealImage, imageFromCamera;
    EditText getMealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal);

        imageToUpload = (ImageView)findViewById(R.id.uploadImage);
        mealImage = (ImageView)findViewById(R.id.meal_image_view);
        imageFromCamera = (ImageView)findViewById(R.id.camera_image_view);

        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        imageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE);
            }
        });
    }

    // When user selects image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData(); // uniform resource indicator
            mealImage.setImageURI(selectedImage);
        }

        if(requestCode == RESULT_CAMERA_IMAGE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            mealImage.setImageBitmap(bitmap);
        }
    }
}
