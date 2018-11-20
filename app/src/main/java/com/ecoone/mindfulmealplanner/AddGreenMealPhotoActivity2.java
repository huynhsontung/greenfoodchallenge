package com.ecoone.mindfulmealplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AddGreenMealPhotoActivity2 extends AppCompatActivity {

    GridView myGridView;
    ArrayList<Bitmap> photos;
    Button takePhoto;
    private static final int GALLERY = 5;
    private static final int CAMERA = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal_photo2);
        photos = new ArrayList<Bitmap>();

        myGridView = (GridView)findViewById(R.id.add_green_meal_photo_grid);
        takePhoto = (Button)findViewById(R.id.test_button);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

    }


    private void showPictureDialog() {
        //Context mContext = getContext();
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(AddGreenMealPhotoActivity2.this);
        pictureDialog.setTitle("Upload photo");
        pictureDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        String[] pictureDialogItems = {"Select from gallery", "Take photo"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i){
                    case 0:
                        choosePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY){
            if(data != null){
                Uri contentURI = data.getData();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(AddGreenMealPhotoActivity2.this.getContentResolver(), contentURI);
                    photos.add(mBitmap);
                    GridAdapter gridAdapter = new GridAdapter(AddGreenMealPhotoActivity2.this, photos);
                    myGridView.setAdapter(gridAdapter);
                    //mealImage.setImageBitmap(mBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAMERA){
            Bitmap cameraBitmap = (Bitmap)data.getExtras().get("data");
            photos.add(cameraBitmap);
            GridAdapter gridAdapter = new GridAdapter(AddGreenMealPhotoActivity2.this, photos);
            myGridView.setAdapter(gridAdapter);
        }

    }
}
