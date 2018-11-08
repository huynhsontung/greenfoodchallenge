package com.ecoone.mindfulmealplanner.Pledge;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.Tool.Calculator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.util.ArrayList;
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
    private MyPledgeViewModel mViewModel;

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
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyPledgeViewModel.class);
        myRecyclerView = view.findViewById(R.id.discover_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);
        setupDatabaseTransaction(view);
        updateRecycler();

    }

    private void setupDatabaseTransaction(View view) {
        totalPledgeAmountText = view.findViewById(R.id.discover_total_pledge_textview);
        totalPledgeNumberText = view.findViewById(R.id.discover_number_pledges_textview);
        totalPledgeAverageText = view.findViewById(R.id.discover_average_textview);
        relevantInfoText = view.findViewById(R.id.discover_equivalence_textview);
        mViewModel.totalPledgeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> data = (Map<String, Object>) dataSnapshot.getValue();
                Long totalPledgeAmount = (Long) data.get("totalAmountSum");
                Long totalPledgeNumber = (Long) data.get("totalNumber");
                Long totalPledgeAverage = totalPledgeAmount/totalPledgeNumber;
                totalPledgeAmountText.setText(getString(R.string.total_pledge_amount,totalPledgeAmount.toString()+"kg"));
                totalPledgeNumberText.setText(getString(R.string.total_number_pledges,totalPledgeNumber));
                totalPledgeAverageText.setText(getString(R.string.average_co2e_saved_per_person,totalPledgeAverage.toString()+"kg/person"));
                int relevantInfoChooser = new Random().nextInt(2);
                int treesPlanted = (int) Calculator.calculateTreesPlanted((float)totalPledgeAmount);
                int kmSaved = (int) Calculator.calculateSavingsInKm((float)totalPledgeAmount);
                if (relevantInfoChooser == 0){
                    relevantInfoText.setText(getString(R.string.pledge_relevant_info,"planting "+String.valueOf(treesPlanted)+" trees"));
                } else {
                    relevantInfoText.setText(getString(R.string.pledge_relevant_info, "saving "+ String.valueOf(kmSaved)+" of driving"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Post: Gets list of all people who pledged in Vancouver.
    //       Includes name, pledged, icon, municipality
    private void updateRecycler() {
        List<PeoplePledging> testPledgingList = new ArrayList<PeoplePledging>();
        PeoplePledging testPeter = new PeoplePledging("Peter Tran", 10,
                R.drawable.edit, "Vancouver");

        PeoplePledging testOther = new PeoplePledging("Joe Tran", 10,
                R.drawable.edit, "Burnaby");

        // -------------------------------TESTING-------------------------------//
        testPledgingList.add(testPeter);
        testPledgingList.add(testOther);
        testPledgingList.add(testPeter);
        testPledgingList.add(testOther);
        testPledgingList.add(testPeter);
        // -------------------------------TESTING-------------------------------//
        myAdapter = new PeoplePledgeAdapter(testPledgingList);
        myRecyclerView.setAdapter(myAdapter);
    }

    private class PeoplePledgeHolder extends RecyclerView.ViewHolder {

        private TextView myPeoplePledgeName;
        private TextView myPeoplePledgeCity;
        private TextView myPeoplePledgeAmount;
        private ImageView myPeoplePledgeIcon;

        public PeoplePledgeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.view_discover_list, parent, false));
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
}
