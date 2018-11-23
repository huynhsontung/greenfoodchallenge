package com.ecoone.mindfulmealplanner.explore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private RecyclerView tabView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private SearchView mSearchView;
    private ListView lListView;

    private GridView gridview;
    private FloatingActionButton addMealAction;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private DatabaseReference mDatabase;


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
        addMealAction = view.findViewById(R.id.add_meal_floating_action);
        tabView =(RecyclerView) view.findViewById(R.id.tab_recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(communication);
        tabView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        tabView.setLayoutManager(layoutManager);
        gridview =(GridView)view.findViewById(R.id.explore_content);

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ExploreDetailActivity.class);
                startActivity(intent);
            }
        });

        addMealAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        setupFirebaseCommunication();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void setupFirebaseCommunication() {
        mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
        mDatabase.child("publicMeals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

//            Bundle bundle2 = new Bundle();
//            bundle2.putString("hahaha123", city_name);
//            exploreFragment.setArguments(bundle2);
//            FragmentManager manager2=getFragmentManager();
//            FragmentTransaction transaction2=manager2.beginTransaction();
//            transaction2.replace(R.id.main_content,exploreFragment).commit();
            ImageAdapter adapter = new ImageAdapter(getActivity(), city_name);
            gridview.setAdapter(adapter);

            }

    };


}