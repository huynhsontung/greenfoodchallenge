package com.ecoone.mindfulmealplanner.addmeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.Food;
import com.ecoone.mindfulmealplanner.database.Meal;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDetailFragment extends Fragment {
    public static AddDetailFragment newInstance() {

        Bundle args = new Bundle();

        AddDetailFragment fragment = new AddDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AddDetailFragment)";
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;
    private AddGreenMealActivity mCallback;
    private AddGreenMealViewModel mViewModel;
    private Toolbar mToolbar;
    private AutoCompleteTextView autoCompleteFoodNameTextView;
    private ImageView foodPhotoImageView;
    private ImageView foodPhotoIcon;
    private FrameLayout addPhotoLayout;
    private ChipGroup mChipGroup;
    private Chip[] chipList;
    private Button submitButton;
    private View mainView;
    private ArrayAdapter mAdapter;

    private int chipNumber;
    private String restaurantName;
    private boolean isGreen;
    private HashMap<String, Object> foodMenu;
    private ArrayList<String> foodNameList;
    private String foodName;
    private Bitmap photo;

    private String mCurrentPhotoPath;

    public void setCallback(Activity activity) {
        this.mCallback = (AddGreenMealActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_add_meal_add_detail, container, false);

        mViewModel = ViewModelProviders.of(getActivity()).get(AddGreenMealViewModel.class);
//        mToolbar = view.findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        setTitle("Add Meal Food");
        foodPhotoIcon = mainView.findViewById(R.id.add_photo_icon);
        foodPhotoImageView = mainView.findViewById(R.id.add_green_meal_photo_image);
        addPhotoLayout = mainView.findViewById(R.id.add_photo_layout);
        autoCompleteFoodNameTextView = mainView.findViewById(R.id.add_green_meal_photo_auto);
        mChipGroup = mainView.findViewById(R.id.add_green_meal_photo_chipgroup);
        submitButton = mainView.findViewById(R.id.submit_food_button);

        foodNameList = new ArrayList<>();

        setupMealObserver();
        initialChipGroup(mainView);
        setSubmitButtonAction();


        addPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        return mainView;
    }

    private void setupMealObserver() {
       mViewModel.meal.observe(this, new Observer<Meal>() {
           @Override
           public void onChanged(Meal meal) {
               if (meal != null){
                   mainView.invalidate();
                   restaurantName = meal.restaurantName;
                   isGreen = meal.isGreen;
                   initialAutoCompleteTextView();
               }
           }
       });
    }


    private void initialAutoCompleteTextView() {
        if (foodMenu == null) {
            getRestaurantFoodMenu(restaurantName).addOnSuccessListener( result -> {
                foodMenu = result;

                if (foodMenu != null) {
                    foodNameList.addAll(foodMenu.keySet());
                    mAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, foodNameList);
                    autoCompleteFoodNameTextView.setAdapter(mAdapter);
                }
            });

            // For food in database
            autoCompleteFoodNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    foodName = (String) parent.getItemAtPosition(position);
                    Log.i(TAG, CLASSTAG + "click food name: " + foodName);
                    setChipGroupView();
                }
            });

            // library from https://github.com/yshrsmz/KeyboardVisibilityEvent
            KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
                @Override
                public void onVisibilityChanged(boolean isOpen) {
                    Log.i(TAG, CLASSTAG + "isOpen: " + isOpen);
                    if (foodNameList.contains(autoCompleteFoodNameTextView.getText().toString())) {
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
    }

    private void initialChipGroup(View view) {
        chipNumber = mChipGroup.getChildCount();
        chipList = new Chip[chipNumber];
        for (int i = 0; i < chipNumber; i++) {
            chipList[i] = view.findViewById(mChipGroup.getChildAt(i).getId()) ;
        }
    }

    private void setSubmitButtonAction() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo == null) {
                    showCustomToast("Please enter a food photo.");
                }
                else if (autoCompleteFoodNameTextView.getText().toString().equals("")) {
                    showCustomToast("Please enter a food name.");
                }
                else if (!isAtLeastOneChipChecked()) {
                    showCustomToast("Please check at least one chip.");
                }
                else {
                    hideKeyboard();
                    if (isGreen) {
                        if (foodNameList.contains(autoCompleteFoodNameTextView.getText().toString()) ) {
                            sendFoodDataBack();
                        }
                        else {
                            showCustomToast("Can not add a food to green restaurant");
                            autoCompleteFoodNameTextView.setText("");
                            for (int i = 0; i < chipNumber; i++) {
                                chipList[i].setChecked(false);
                            }
                        }
                    }
                    else {
                        sendFoodDataBack();
                    }
                    resetViews();
                }
            }
        });
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void resetViews(){
        foodPhotoImageView.setImageResource(android.R.color.transparent);
        foodPhotoIcon.setVisibility(View.VISIBLE);
        autoCompleteFoodNameTextView.setText("");
        autoCompleteFoodNameTextView.clearListSelection();
    }

    private void sendFoodDataBack() {

        HashMap<String, Integer> ingredient = new HashMap<>();
        Food food = new Food();

        if (foodNameList.contains(foodName)) {
            HashMap<String, Object> foodDetail = (HashMap<String, Object>) foodMenu.get(foodName);
            ingredient = (HashMap<String, Integer>) foodDetail.get("ingredient");
            food.foodName = foodName;
        }
        else {
            food.foodName = autoCompleteFoodNameTextView.getText().toString();
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

        Meal meal = mViewModel.meal.getValue();
        food.photoBitmap = photo;
        meal.foodList.put(food.foodName,food);
        mViewModel.meal.setValue(meal);
        mCallback.switchPage();

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
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Upload photo");
        pictureDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        String[] pictureDialogItems = getResources().getStringArray(R.array.add_photo_options);
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

//    // copy from https://developer.android.com/training/camera/photobasics
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getFilesDir();
//        File image = new File(storageDir, imageFileName);
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Log.e(TAG, CLASSTAG + " can not create file");
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.ecoone.mindfulmealplanner", photoFile);
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(takePictureIntent,
//                        PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo activity : cameraActivities) {
//                    grantUriPermission(activity.activityInfo.packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                }
//
//                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
//            }
//        }
//    }

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
        getActivity().sendBroadcast(mediaScanIntent);
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
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Log.i(TAG, CLASSTAG + "photo before: " + mBitmap.getByteCount()+ " " + mBitmap.getHeight()+ " " + mBitmap.getWidth());
                    foodPhotoImageView.setImageBitmap(mBitmap);
                    photo = mBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if(requestCode == REQUEST_CAMERA){
//            File f = new File(mCurrentPhotoPath);
//            Uri contentURI = Uri.fromFile(f);
//            try{
//                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                foodPhotoImageView.setImageBitmap(mBitmap);
//            } catch (IOException e) {
//
//            }

            Bitmap mBitmap = (Bitmap)data.getExtras().get("data");
            photo = mBitmap;
            foodPhotoImageView.setImageBitmap(mBitmap);

//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            photo = stream.toByteArray();

//            galleryAddPic();
        }
        foodPhotoIcon.setVisibility(View.GONE);

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

    private void showCustomToast(String message) {
        Toast mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }
}
