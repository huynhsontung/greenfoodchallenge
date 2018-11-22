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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;
import java.util.Objects;

public class ExploreFragment extends Fragment {

    private int[] images_id={R.drawable.surrey,R.drawable.anmore,R.drawable.vancouver};
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;




    private SearchView mSearchView;
    private ListView lListView;

    GridView gridview;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ExploreFragment)";


    public static ExploreFragment newInstance() {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),getData(),communication);
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutmanager);

        String ioi = getArguments().getString("hahaha123");
        Toast.makeText(getContext(),ioi,Toast.LENGTH_SHORT).show();


        gridview =(GridView)view.findViewById(R.id.gridview1);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExploreDetail exploreDetail= new ExploreDetail();
                Bundle args = new Bundle();
                args.putInt("image_position",position);
                exploreDetail.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_content,exploreDetail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        return view;
    }

    public static List<scroll_item_data> getData() {
        List<scroll_item_data>data=new ArrayList<>();
        String[] city_names = scroll_item_data.title;
        int[] city_pics = scroll_item_data.picturePath;

        for (int i=0;i<city_names.length;i++){
            scroll_item_data current=new scroll_item_data();
            current.title[i]=(city_names[i]);
            current.picturePath[i]=(city_pics[i]);
            data.add(current);
        }
        return data;
    }

    FragmentCommunication communication=new FragmentCommunication() {
        @Override
        public void respond(int position, String city_name, int citypics) {
            ExploreFragment exploreFragment = new ExploreFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("hahaha123", city_name);
            exploreFragment.setArguments(bundle2);
            FragmentManager manager2=getFragmentManager();
            FragmentTransaction transaction2=manager2.beginTransaction();
            transaction2.replace(R.id.main_content,exploreFragment).commit();

            }

    };





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, CLASSTAG + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, CLASSTAG + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, CLASSTAG + " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}