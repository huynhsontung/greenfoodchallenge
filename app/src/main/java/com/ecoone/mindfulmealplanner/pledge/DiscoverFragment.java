package com.ecoone.mindfulmealplanner.pledge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DiscoverFragment extends Fragment {
    private List<PeoplePledging> myPeoplePledgingList;
    private RecyclerView myRecyclerView;
    private PeoplePledgeAdapter myAdapter;
    private TextView totalPledgeAmountText;
    private TextView totalPledgeNumberText;
    private TextView totalPledgeAverageText;
    private TextView relevantInfoText;
    private Spinner filterSpinner;
    private ImageView helpIconDiscover;
    private PledgeViewModel mViewModel;
    ValueEventListener totalPledgeListener;
    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pledge_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PledgeViewModel.class);
        myRecyclerView = view.findViewById(R.id.discover_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);
        setupDatabaseTransaction(view);
        setupMunicipalityFilter(view);
        updateRecycler();
        helpIconDiscover = view.findViewById(R.id.help_icon_discover);
        discoverTutorialListener();

    }

    public void discoverTutorialListener() {
        helpIconDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDiscoverTutorial();
            }
        });
    }

    // starts tutorial if its the first time being shown
    public void startDiscoverTutorial() {
        Drawable d = getResources().getDrawable(R.drawable.cabbage_icon);
        final BubbleShowCase bubble1 = new BubbleShowCaseBuilder(getActivity())
                .title("Explore pledges from people all over the Lower Mainland!")
                .titleTextSize(18)
                .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM)
                .image(d)
                .targetView(filterSpinner)
                .show();
    }

    private void setupMunicipalityFilter(View view) {
        filterSpinner = view.findViewById(R.id.discover_filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.location,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.cityFilter.setValue((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mViewModel.cityFilter.setValue("Vancouver");
            }
        });
    }

    private void setupDatabaseTransaction(View view) {
        totalPledgeAmountText = view.findViewById(R.id.discover_total_pledge_textview);
        totalPledgeNumberText = view.findViewById(R.id.discover_number_pledges_textview);
        totalPledgeAverageText = view.findViewById(R.id.discover_average_textview);
        relevantInfoText = view.findViewById(R.id.discover_equivalence_textview);
        totalPledgeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();
                Long totalPledgeAmount = (Long) data.get("totalAmountSum");
                Long totalPledgeNumber = (Long) data.get("totalNumber");
                Long totalPledgeAverage = totalPledgeAmount/((totalPledgeNumber>0)?totalPledgeNumber:1);
                totalPledgeAmountText.setText(getString(R.string.total_pledge_amount,totalPledgeAmount.toString()+"kg"));
                totalPledgeNumberText.setText(getString(R.string.total_number_pledges,totalPledgeNumber));
                totalPledgeAverageText.setText(getString(R.string.average_co2e_saved_per_person,totalPledgeAverage.toString()+"kg/person"));
                int relevantInfoChooser = new Random().nextInt(2);
                int treesPlanted = (int) Calculator.calculateTreesPlanted((float)totalPledgeAmount/1000);
                int kmSaved = (int) Calculator.calculateSavingsInKm((float)totalPledgeAmount/1000);
                if (relevantInfoChooser == 0){
                    relevantInfoText.setText(getString(R.string.pledge_relevant_info,"planting "+String.valueOf(treesPlanted)+" trees"));
                } else {
                    relevantInfoText.setText(getString(R.string.pledge_relevant_info, "saving "+ String.valueOf(kmSaved)+"km of driving"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mViewModel.totalPledgeReference.addValueEventListener(totalPledgeListener);
    }

    // Post: Gets list of all people who pledged in Vancouver.
    //       Includes name, pledged, icon, municipality
    private void updateRecycler() {

        mViewModel.cityFilter.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String city) {
                if(city!=null)
                    getUsersDataByLocation(city).addOnCompleteListener(new OnCompleteListener<Map<String, Object>>() {
                        @Override
                        public void onComplete(@NonNull Task<Map<String, Object>> usersData) {
                            mViewModel.userList.setValue(usersData.getResult());
                        }
                    });
            }
        });
        mViewModel.userList.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                List<PeoplePledging> pledgingList = new ArrayList<>();
                if(!stringObjectMap.isEmpty()){
                    for(Map.Entry<String,Object> entry: stringObjectMap.entrySet()){
                        Map<String,Object> userData = (Map<String, Object>) entry.getValue();
                        String displayName = (String) userData.get("displayName");
                        String iconName = (String) userData.get("iconName");
                        int pledgeAmount = (int) userData.get("amount");
                        pledgingList.add(new PeoplePledging(
                                displayName,
                                pledgeAmount,
                                getDrawableIdbyName(iconName),
                                mViewModel.cityFilter.getValue()));
                    }
                    myAdapter = new  PeoplePledgeAdapter(pledgingList);
                } else {
                    myAdapter = new PeoplePledgeAdapter(pledgingList);
                }
                myRecyclerView.setAdapter(myAdapter);
            }
        });
        myAdapter = new PeoplePledgeAdapter(new ArrayList<PeoplePledging>());
        myRecyclerView.setAdapter(myAdapter);
    }

    private class PeoplePledgeHolder extends RecyclerView.ViewHolder {

        private TextView myPeoplePledgeName;
        private TextView myPeoplePledgeCity;
        private TextView myPeoplePledgeAmount;
        private ImageView myPeoplePledgeIcon;

        public PeoplePledgeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.component_view_discover_list, parent, false));
            //itemView.setOnClickListener(this);

            myPeoplePledgeName = itemView.findViewById(R.id.discover_list_name);
            myPeoplePledgeCity = itemView.findViewById(R.id.discover_view_municipality);
            myPeoplePledgeAmount = itemView.findViewById(R.id.discover_view_amount);
            myPeoplePledgeIcon = itemView.findViewById(R.id.discover_list_icon);
        }

        public void bind(PeoplePledging personPledging) {
            myPeoplePledgeName.setText(personPledging.getNameUser());
            myPeoplePledgeCity.setText("Municipality: " + personPledging.getMunicipality());
            myPeoplePledgeAmount.setText("Pledge: " + String.valueOf(personPledging.getPledged() ) + "g");
            myPeoplePledgeIcon.setImageResource(personPledging.getIconID());
        }
    }


    private Task<Map<String, Object>> getUsersDataByLocation(String location) {

        Map<String, Object> data = new HashMap<>();
        data.put("location", location);

        return FirebaseFunctions.getInstance().getHttpsCallable("getUsersDataByLocation")
                .call(data).continueWith(new Continuation<HttpsCallableResult, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        return result;
                    }
                });
    }

    private class PeoplePledgeAdapter extends RecyclerView.Adapter<PeoplePledgeHolder>{
        private List<PeoplePledging> mPeoplePledging;

        public PeoplePledgeAdapter(List<PeoplePledging> myPeoplePledgingList) {
            mPeoplePledging = myPeoplePledgingList;
        }

        @NonNull
        @Override
        public PeoplePledgeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PeoplePledgeHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull PeoplePledgeHolder peoplePledgeHolder, int i) {
            PeoplePledging peoplePledging = mPeoplePledging.get(i);
            peoplePledgeHolder.bind(peoplePledging);
        }

        @Override
        public int getItemCount() {
            return mPeoplePledging.size();
        }
    }

    private int getDrawableIdbyName(String name) {
        if(name==null)
            return R.drawable.android;
        int resourceId = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
        return resourceId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.totalPledgeReference.removeEventListener(totalPledgeListener);
    }
}
