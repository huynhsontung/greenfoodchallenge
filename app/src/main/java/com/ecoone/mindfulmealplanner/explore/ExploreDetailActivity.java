package com.ecoone.mindfulmealplanner.explore;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface.deleteMeal;
import static com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface.getUid;

public class ExploreDetailActivity extends AppCompatActivity {

    private TextView mealTitle;
    private TextView restaurantName;
    private TextView location;
    private TextView description;
    private TextView likeCounter;
    private ImageButton closeButton;
    private CircleImageView authorIcon;
    private TextView authorName;
    private CheckBox favoriteCheckbox;
    private ViewPager foodImagesPager;
    private ExploreDetailViewModel mViewModel;
    private Toolbar optionMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_detail);
        optionMenu = findViewById(R.id.option_menu);
        setSupportActionBar(optionMenu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        likeCounter = findViewById(R.id.like_counter);
        foodImagesPager = findViewById(R.id.detail_images_pager);
        mealTitle = findViewById(R.id.meal_title);
        description = findViewById(R.id.description);
        restaurantName = findViewById(R.id.restaurant_name);
        location = findViewById(R.id.location);
        authorName = findViewById(R.id.author_name);
        authorIcon = findViewById(R.id.user_icon);
        favoriteCheckbox = findViewById(R.id.action_favorite);
        favoriteCheckbox.setOnClickListener(view -> {
            CheckBox like = (CheckBox) view;
            if(like.isChecked()){
                mViewModel.likes.setValue(mViewModel.likes.getValue()+1);
                // call server function
            } else {
                mViewModel.likes.setValue(mViewModel.likes.getValue()-1);
                // call server function
            }
        });
        closeButton = findViewById(R.id.detail_close);
        closeButton.setOnClickListener(v -> onBackPressed());

        mViewModel = ViewModelProviders.of(this).get(ExploreDetailViewModel.class);
        initializeData();
        setupContent();
    }

    private void initializeData() {
        if (mViewModel.foodNames.size() >= 1){
            return;
        }
        mViewModel.mealIdentifier = getIntent().getStringExtra("identifier");
        HashMap<String, Object> mealObj = (HashMap<String, Object>) getIntent().getSerializableExtra("mealObj");
        HashMap<String, Object> author = (HashMap<String, Object>) mealObj.get("author");
        HashMap<String, Object> foodList = (HashMap<String, Object>) mealObj.get("foodList");
        HashMap<String, Integer> metrics = (HashMap<String, Integer>) mealObj.get("metrics");
        mViewModel.likes.setValue(metrics.get("likes"));
        mViewModel.mealName = (String) mealObj.get("mealName");
        mViewModel.mealDescription = (String) mealObj.get("mealDescription");
        mViewModel.restaurantName = (String) mealObj.get("restaurantName");
        mViewModel.authorName = (String) author.get("displayName");
        mViewModel.authorIcon = (String) author.get("iconName");
        mViewModel.authorUid = (String) author.get("userUid");
        for (int i = 0; i< foodList.size(); i++){
            String foodName = (String) foodList.keySet().toArray()[i];
            HashMap<String, Object> food = (HashMap<String, Object>) foodList.get(foodName);
            mViewModel.foodNames.add(foodName);
            mViewModel.photoRefs.add((String) food.get("storageReference"));
        }
    }

    private void setupContent() {
        mealTitle.setText(mViewModel.mealName);
        authorName.setText(mViewModel.authorName);
        description.setText(mViewModel.mealDescription);
        restaurantName.setText(mViewModel.restaurantName);
        likeCounter.setText(String.valueOf(mViewModel.likes));
        mViewModel.likes.observe(this, integer -> {
            if (integer != null)
                likeCounter.setText(String.valueOf(integer));
        });
        int iconId = getResources()
                .getIdentifier(mViewModel.authorIcon, "drawable", getPackageName());
        authorIcon.setImageResource(iconId);
        location.setText("Vancouver");

        foodImagesPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            ArrayList<ImagesPagerFragment> fragmentKeeper = new ArrayList<>(mViewModel.photoRefs.size());
            @Override
            public Fragment getItem(int i) {
                if (fragmentKeeper.size() > i){
                    return fragmentKeeper.get(i);
                } else {
                    ImagesPagerFragment fragment = ImagesPagerFragment.newInstance();
                    try {
                        fetchImage(mViewModel.photoRefs.get(i), mViewModel.foodNames.get(i), fragment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fragmentKeeper.add(fragment);
                    return fragment;
                }
            }

            private void fetchImage(@NonNull String refUrl, String foodName, @NonNull ImagesPagerFragment fragment) throws IOException {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReferenceFromUrl(refUrl);
                File tmpFile = File.createTempFile(foodName, "jpg");
                if(!tmpFile.canWrite()){
                    return;
                }
                imageRef.getFile(tmpFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(tmpFile.getPath());
                    fragment.updateImageView(bitmap);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    finish();
                });
            }

            @Override
            public int getCount() {
                return mViewModel.photoRefs.size();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getUid().equals(mViewModel.authorUid)) {
            menu.add("Delete meal").setOnMenuItemClickListener(item -> {
                deleteMeal(mViewModel.mealIdentifier);
                finish();
                return true;
            });
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    public static class ImagesPagerFragment extends Fragment{
        public static ImagesPagerFragment newInstance() {
            Bundle args = new Bundle();
            ImagesPagerFragment fragment = new ImagesPagerFragment();
            fragment.setArguments(args);
            return fragment;
        }

        private Bitmap bitmap;
        private ImageView imageView;

        public void updateImageView(Bitmap image) {
            bitmap = image;
            imageView.post(() -> imageView.setImageBitmap(image));
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.component_framelayout_imageview, container, false);
            imageView = view.findViewById(R.id.imageview_in_framelayout);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            return view;
        }
    }

}