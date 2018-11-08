package com.ecoone.mindfulmealplanner.Pledge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Pledge.PeoplePledging;
import com.ecoone.mindfulmealplanner.R;


import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<PeoplePledging> myPeoplePledgingList;
    private RecyclerView myRecyclerView;
    private PeoplePledgeAdapter myAdapter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        myRecyclerView = view.findViewById(R.id.discover_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);

        updateRecycler();

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
