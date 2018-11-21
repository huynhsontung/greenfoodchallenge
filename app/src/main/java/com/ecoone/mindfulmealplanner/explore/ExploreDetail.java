package com.ecoone.mindfulmealplanner.explore;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.User;
import com.ecoone.mindfulmealplanner.pledge.PledgeFragment;
import com.ecoone.mindfulmealplanner.profile.ProfileFragment;
import com.ecoone.mindfulmealplanner.profile.account.UserAccountActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.profile.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ExploreDetail extends Fragment {
    private int[] images_id={R.drawable.surrey,R.drawable.anmore,R.drawable.vancouver};
    private String[] image_name = {"surrey","anmore","vancouver"};

    public static ExploreDetail newInstance() {

        Bundle args = new Bundle();

        ExploreDetail fragment = new ExploreDetail();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int pos = getArguments().getInt("image_position");
        String i = getArguments().getString("hahaha123");
        Toast.makeText( getActivity(), i , Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_explore_detail, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.detail_image);
        imageView.setImageResource(images_id[pos]);
        TextView textView = (TextView) view.findViewById(R.id.detail_text);
        textView.setText(image_name[pos]);
        TextView des = (TextView) view.findViewById(R.id.scroll_text);
        des.setText("sdfgsdhfgsfghsdf\n shafgasgfuisgfuisdgfjksdghfjhdsgfjsdhgfuisdgfuisdfguisdfgsiuf\ndsfghjdshgfhjsdfghjsfg\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\n");
        des.setMovementMethod(new ScrollingMovementMethod());
        Button btn = (Button) view.findViewById(R.id.detail_back);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ExploreFragment exploreFragment= new ExploreFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_content,exploreFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        return  view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }





}