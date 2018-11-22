package com.ecoone.mindfulmealplanner.explore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreDetailActivity extends AppCompatActivity {
    private int[] images_id={R.drawable.surrey,R.drawable.anmore,R.drawable.vancouver};
    private String[] image_name = {"surrey","anmore","vancouver"};

    private TextView mealTitle;
    private TextView restaurantName;
    private TextView location;
    private TextView description;
    private ImageButton closeButton;
    private CircleImageView userIcon;
    private TextView username;
    private CheckBox favoriteCheckbox;
    private ViewPager foodImagesPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_explore_detail);

        foodImagesPager = findViewById(R.id.detail_images_pager);
        mealTitle = findViewById(R.id.meal_title);
        description = findViewById(R.id.description);
        restaurantName = findViewById(R.id.restaurant_name);
        location = findViewById(R.id.location);
        username = findViewById(R.id.user_name);
        userIcon = findViewById(R.id.user_icon);
        favoriteCheckbox = findViewById(R.id.action_favorite);
        closeButton = findViewById(R.id.detail_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupFoodImagesPager();
    }

    private void setupFoodImagesPager() {

    }

    public static class ImagesPagerFragment extends Fragment{
        public static ImagesPagerFragment newInstance() {

            Bundle args = new Bundle();

            ImagesPagerFragment fragment = new ImagesPagerFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

}