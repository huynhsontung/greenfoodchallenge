package com.ecoone.mindfulmealplanner.MealTracker.AddMeal.Deprecated;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhotoFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;
    private static final int GALLERY = 5;
    private static final int CAMERA = 6;

    ImageView imageToUpload, mealImage, imageFromCamera;
    private TextView textView;
    public AddPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        //imageToUpload = (ImageView)view.findViewById(R.id.uploadImage);
        mealImage = (ImageView)view.findViewById(R.id.meal_image_view);
        /*imageFromCamera = (ImageView)view.findViewById(R.id.camera_image_view);


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
        });*/

        mealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        return view;
    }

    private void showPictureDialog() {
        //Context mContext = getContext();
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
        /*if(requestCode == RESULT_LOAD_IMAGE  && data != null){
            Uri selectedImage = data.getData(); // uniform resource indicator
            mealImage.setImageURI(selectedImage);
        }

        if(requestCode == RESULT_CAMERA_IMAGE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            mealImage.setImageBitmap(bitmap);
        }*/

        if(requestCode == GALLERY){
            if(data != null){
                Uri contentURI = data.getData();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    mealImage.setImageBitmap(mBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAMERA){
            Bitmap cameraBitmap = (Bitmap)data.getExtras().get("data");
            mealImage.setImageBitmap(cameraBitmap);
        }
    }
}