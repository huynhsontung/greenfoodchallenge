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

import com.ecoone.mindfulmealplanner.addmeal.AddGreenMealActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.Meal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploreFragment extends Fragment implements FilterListAdapter.FilterListCallback {

    private RecyclerView filterList;
    private FilterListAdapter filterListAdapter;

    private SearchView mSearchView;
    private ListView lListView;

    private GridView mealGrid;
    private FloatingActionButton addMealAction;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private DatabaseReference mDatabase;
    private HashMap<String, Object> mealsData;
    private HashMap<String, Object> mealList;
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private Meal mealDataForPassing = new Meal();
    private MealImageAdapter mealImageAdapter;
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ExploreFragment)";
    private final int defaultRange = 10;


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
        setupFilterList(view);
        setupAddMealButton(view);
        setupMealGrid(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setupMealGrid(View view) {
        mealGrid = view.findViewById(R.id.explore_content);
        mealGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMealSelect(view);
            }
        });
        queryMeals("popular", defaultRange, null).addOnSuccessListener(updateMealGrid);
    }

    private void onMealSelect(View view) {
        String mealName = (String) view.getTag();
        HashMap<String, Object> mealList = mealImageAdapter.getMealList();
        HashMap<String, Object> mealObj = (HashMap<String, Object>) mealList.get(mealName);
        if (mealObj == null){
            return;
        }
        Intent intent = new Intent(getActivity(), ExploreDetailActivity.class);
        intent.putExtra("mealObj", mealObj);
        intent.putExtra("identifier", mealName);
        startActivity(intent);
    }

    private void setupAddMealButton(View view) {
        addMealAction = view.findViewById(R.id.add_meal_floating_action);
        addMealAction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddGreenMealActivity.class);
            startActivity(intent);
        });
    }

    private void setupFilterList(View view) {
        filterList = view.findViewById(R.id.tab_recycler_view);
        filterListAdapter = new FilterListAdapter(this);
        filterList.setAdapter(filterListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        filterList.setLayoutManager(layoutManager);
    }

    @Override
    public void onFilterSelect(int position, String filter) {
        switch (filter){
            case "Most Recent":
                filter = "recent";
                break;
            case "My Meals":
                filter = "self";
                break;
        }
        queryMeals(filter, defaultRange, null).addOnSuccessListener(updateMealGrid);
    }

    OnSuccessListener<HashMap<String, Object>> updateMealGrid = result -> {
        if(result == null){
            Toast.makeText(getContext(), "There's currently no meal to display. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }
        mealImageAdapter = new MealImageAdapter(getActivity(), result);
        mealGrid.setAdapter(mealImageAdapter);
    };

    private Task<HashMap<String, Object>> queryMeals(String filter, int range, String lastQueryEndpoint) {
        Map<String, Object> data = new HashMap<>();
        data.put("filter", filter);
        data.put("range", range);
        data.put("last", lastQueryEndpoint);
        return mFunctions.getHttpsCallable("getFilteredMealList")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }


    public static List<FilterOptions> getData() {
        List<FilterOptions>data=new ArrayList<>();
        String[] city_names = FilterOptions.title;
        int[] city_pics = FilterOptions.picturePath;

        for (int i=0;i<city_names.length;i++){
            FilterOptions current=new FilterOptions();
            current.title[i]=(city_names[i]);
            current.picturePath[i]=(city_pics[i]);
            data.add(current);
        }
        return data;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


}