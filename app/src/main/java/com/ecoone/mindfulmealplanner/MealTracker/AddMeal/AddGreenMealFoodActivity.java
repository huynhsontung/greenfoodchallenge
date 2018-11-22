package com.ecoone.mindfulmealplanner.MealTracker.AddMeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.Food;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGreenMealFoodActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AddGreenMealFoodActivity)";
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;


    private Toolbar mToolbar;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView foodPhotoImageView;
    private ChipGroup mChipGroup;
    private Chip[] chipList;
    private Button submitButton;

    private ArrayAdapter mAdapter;

    private int chipNumber;
    private String restaurantName;
    private boolean isGreen;
    private int co2eAmount;
    private HashMap<String, Object> foodMenu;
    private ArrayList<String> foodNameList;
    private String foodName;
    private byte[] photoByteArray;

    private String mCurrentPhotoPath;

    public static Intent newIntent(Context packageContext, String restaurantName, boolean isGreen) {
        Intent intent = new Intent(packageContext, AddGreenMealFoodActivity.class);
        intent.putExtra("restaurantName", restaurantName);
        intent.putExtra("isGreen", isGreen);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal_photo);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Meal Food");

        foodPhotoImageView = findViewById(R.id.add_green_meal_photo_image);
        autoCompleteTextView = findViewById(R.id.add_green_meal_photo_auto);
        mChipGroup = findViewById(R.id.add_green_meal_photo_chipgroup);
        submitButton = findViewById(R.id.submit_food_button);

        restaurantName = getIntent().getStringExtra("restaurantName");
        isGreen = getIntent().getBooleanExtra("isGreen", false);
        foodNameList = new ArrayList<>();

        initialChipGroup();
        initialAutoCompleteTextView();
        setSubmitButtonAction();


        foodPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    private void initialAutoCompleteTextView() {
        getRestaurantFoodMenu(restaurantName).addOnCompleteListener(new OnCompleteListener<HashMap<String, Object>>() {
            @Override
            public void onComplete(@NonNull Task<HashMap<String, Object>> task) {
                foodMenu = task.getResult();

                if (foodMenu != null) {
                    foodNameList.addAll(foodMenu.keySet());
                    mAdapter = new ArrayAdapter<>(AddGreenMealFoodActivity.this,
                            android.R.layout.simple_list_item_1, foodNameList);
                    autoCompleteTextView.setAdapter(mAdapter);
                }
            }
        });

        // For food in database
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodName = (String) parent.getItemAtPosition(position);
                Log.i(TAG, CLASSTAG + "click food name: " + foodName);
                setChipGroupView();
            }
        });


        // library from https://github.com/yshrsmz/KeyboardVisibilityEvent
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                Log.i(TAG, CLASSTAG + "isOpen: " + isOpen);
                if (foodNameList.contains(autoCompleteTextView.getText().toString())) {
                    setChipGroupView();
                }
                else {
                    for (int i = 0; i < chipNumber; i++) {
                        chipList[i].setChecked(false);
                        setChipGroupEditableStatus(1);
                    }
                }
            }
        });
    }

    private void initialChipGroup() {
        chipNumber = mChipGroup.getChildCount();
        chipList = new Chip[chipNumber];
        for (int i = 0; i < chipNumber; i++) {
            chipList[i] = findViewById(mChipGroup.getChildAt(i).getId()) ;
        }
    }

    private void setSubmitButtonAction() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoByteArray == null) {
                    showCustomToast("Please enter a food photo.");
                }
                else if (autoCompleteTextView.getText().toString().equals("")) {
                    showCustomToast("Please enter a food name.");
                }
                else if (!isAtLeastOneChipChecked()) {
                    showCustomToast("Please check at least one chip.");
                }
                else {
                    if (isGreen) {
                        if (foodNameList.contains(autoCompleteTextView.getText().toString()) ) {
                            sendFoodDataBack();
                        }
                        else {
                            showCustomToast("Can not add a food to green restaurant");
                            autoCompleteTextView.setText("");
                            for (int i = 0; i < chipNumber; i++) {
                                chipList[i].setChecked(false);
                            }
                        }
                    }
                    else {
                        sendFoodDataBack();
                    }
                }
            }
        });
    }

    private void sendFoodDataBack() {

        HashMap<String, Integer> ingredient = new HashMap<>();
        Food food = new Food();

        if (foodNameList.contains(foodName)) {
            HashMap<String, Object> foodDetail = (HashMap<String, Object>) foodMenu.get(foodName);
            ingredient = (HashMap<String, Integer>) foodDetail.get("ingredient");
            co2eAmount = (Integer) foodDetail.get("co2eAmount");
            food.foodName = foodName;
            food.co2eAmount = co2eAmount;
        }
        else {
            food.foodName = autoCompleteTextView.getText().toString();
            food.co2eAmount = 0;
        }


        for (int i = 0; i < chipNumber; i++) {
            String chipName = chipList[i].getText().toString().toLowerCase();
            if (isGreen) {
                food.ingredient.put(chipName, ingredient.get(chipName));
            }
            else {
                food.ingredient.put(chipName, (chipList[i].isChecked() ? -1 : 0));
            }
        }
        setFoodInfoResult(food);
    }

    private void setFoodInfoResult(Food food) {
        Intent data = new Intent();
        data.putExtra("foodInfoResult", food);
        data.putExtra("photoByteResult", photoByteArray);
        setResult(RESULT_OK, data);
        // for food photo
    }

    public static Food getFoodInfo(Intent result) {
        return (Food) result.getSerializableExtra("foodInfoResult");
    }

    public static byte[] getPhotoArraybyte(Intent result) {
        return result.getByteArrayExtra("photoByteResult");
    }

    // set chip view (user can not change the view, different from the listener on chips)
    private void setChipGroupView() {
        setChipGroupEditableStatus(0);
        setChipCheckView();
    }

    private void setChipCheckView() {
        HashMap<String, Object> foodDetail = (HashMap<String, Object>) foodMenu.get(foodName);
        HashMap<String, Integer> ingredient = (HashMap<String, Integer>) foodDetail.get("ingredient");
        for (int i = 0; i < chipNumber; i++) {
//            Chip chip = findViewById(mChipGroup.getChildAt(i).getId()) ;
            String chipName = chipList[i].getText().toString().toLowerCase();
            int ingredientValue = ingredient.get(chipName);
            if (ingredientValue != 0) {
                chipList[i].setChecked(true);
            }
            else {
                chipList[i].setChecked(false);
            }
        }

    }

    private void setChipGroupEditableStatus(int sign) {
        if (sign == 0) {
            for (int i = 0; i < chipNumber; i++) {
                chipList[i].setClickable(false);
            }
        }
        else {
            for (int i = 0; i < mChipGroup.getChildCount(); i++) {
                chipList[i].setClickable(true);
            }
        }

    }

    private boolean isAtLeastOneChipChecked() {
        for (int i = 0; i < chipNumber; i++) {
            if (chipList[i].isChecked()) {
                return true;
            }
        }
        return false;
    }

    private Task<HashMap<String, Object>> getRestaurantFoodMenu(String restaurantName) {
        Map<String, Object> data = new HashMap<>();
        data.put("restaurantName", restaurantName);

        return FirebaseFunctions.getInstance().getHttpsCallable("getRestaurantFoodMenu")
                .call(data).continueWith(new Continuation<HttpsCallableResult, HashMap<String, Object>>() {
                    @Override
                    public HashMap<String, Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                        return result;
                    }
                });
    }

    private void showPictureDialog() {
        //Context mContext = getContext();
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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

    // copy from https://developer.android.com/training/camera/photobasics
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getFilesDir();
        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, CLASSTAG + " cannot create file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ecoone.mindfulmealplanner", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    grantUriPermission(activity.activityInfo.packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    // copy from https://developer.android.com/training/camera/photobasics
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_GALLERY){
            if(data != null){
                Uri contentURI = data.getData();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Log.i(TAG, CLASSTAG + "photo before: " + mBitmap.getByteCount()+ " " + mBitmap.getHeight()+ " " + mBitmap.getWidth());

//                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
//                    Cursor c = getContentResolver().query(contentURI, filePathColumns, null, null, null);
//                    c.moveToFirst();
//                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
//                    String imagePath = c.getString(columnIndex);
//                    c.close();
//                    mBitmap = getScaledBitmap(imagePath, 500, 500);
//                    Log.i(TAG, CLASSTAG + "photo after: " + mBitmap.getByteCount()+ " " + mBitmap.getHeight()+ " " + mBitmap.getWidth());
                    foodPhotoImageView.setImageBitmap(mBitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    photoByteArray = stream.toByteArray();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == REQUEST_CAMERA){
//            File f = new File(mCurrentPhotoPath);
//            Uri contentURI = Uri.fromFile(f);
//            try{
//                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                foodPhotoImageView.setImageBitmap(mBitmap);
//            } catch (IOException e) {
//
//            }
            Bitmap mBitmap = (Bitmap)data.getExtras().get("data");
            foodPhotoImageView.setImageBitmap(mBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            photoByteArray = stream.toByteArray();

//            galleryAddPic();
        }
    }

//    private void saveImageToGallery(Bitmap bitmap) {
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root);
//    }

    // copy from Android Programming: The Big Nerd Ranch Guide (3rd Edition)
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 2;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showCustomToast(String message) {
        Toast mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }

}
