package com.ecoone.mindfulmealplanner.dashboard.planlist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.dashboard.DashboardViewModel;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.Plan;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanListFragment extends Fragment {

    final DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private RecyclerView mRecyclerView;
    private PlanAdapter mAdapter;
    private DashboardViewModel mViewModel;
    private ValueEventListener mValueEventListener;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(PlanListFragment)";

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    View planView;
    private static final String SKIP_TUTORIAL_PLAN_LIST = "planlist";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getParentFragment())).get(DashboardViewModel.class);
        return inflater.inflate(R.layout.fragment_plan_list, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // write your code here
        mRecyclerView = view.findViewById(R.id.plan_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = mSharedPreferences.edit();
        updateUI();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {
            Log.i(TAG,CLASSTAG + "vis in planlist");
            checkForTutorial();
            if (!isVisibleToUser) {
                Log.i(TAG,CLASSTAG + "not vis in planlist");
            }
        }
    }

    // Starts tutorial when fragment first appears
    private void checkForTutorial() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int flag = mSharedPreferences.getInt(SKIP_TUTORIAL_PLAN_LIST,0);
        Drawable d = getResources().getDrawable(R.drawable.cabbage_icon);

        if(flag == 0) {
            final BubbleShowCase bubble1 = new BubbleShowCaseBuilder(getActivity())
                    .title("Simply click on any of your existing plans to make it your current!")
                    .titleTextSize(18)
                    .image(d)
                    .targetView(planView)
                    .show();

            editor.putInt(SKIP_TUTORIAL_PLAN_LIST, 1);
            editor.apply();
            Log.i(TAG,CLASSTAG + "dashplan done");
        }

    }


    private void updateUI() {

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Plan> plans = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Plan plan = snapshot.getValue(Plan.class);
                    plan.planName = snapshot.getKey();
                    plans.add(plan);
                }
                mAdapter = new PlanAdapter(plans);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid)
                .child(FirebaseDatabaseInterface.PLANINFO_NODE)
                .addValueEventListener(mValueEventListener);

    }

    private class PlanHolder extends RecyclerView.ViewHolder {
        private TextView mPlanNameTextView;
        private TextView mPlanFoodAmountTextView;

        public PlanHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_plan, parent, false));

            mPlanNameTextView = itemView.findViewById(R.id.recyclerview_plan_name);
            mPlanFoodAmountTextView = itemView.findViewById(R.id.plan_food_amount);
        }

        public void bind(Plan plan) {
            mPlanNameTextView.setText(plan.planName);
            mPlanNameTextView.setTextColor(Color.BLACK);
            mPlanFoodAmountTextView.setText(FirebaseDatabaseInterface.getPlanDatatoString(plan));
        }
    }

    private class PlanAdapter extends RecyclerView.Adapter<PlanHolder> implements View.OnClickListener{

        private List<Plan> mPlans;

        public  PlanAdapter(List<Plan> plans) {
            mPlans = plans;
        }

        @NonNull
        @Override
        public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PlanHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PlanHolder planHolder, int position) {
            Plan plan = mPlans.get(position);
            planView = planHolder.itemView;
            planHolder.bind(plan);
            planHolder.itemView.setTag(plan);
            planHolder.itemView.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return mPlans.size();
        }

        @Override
        public void onClick(View v) {
            Plan selectedPlan = (Plan) v.getTag();
            mViewModel.getCurrentPlan().setValue(selectedPlan.planName);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Resume" +CLASSTAG);
        updateUI();
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
        Log.d(TAG, CLASSTAG + " onDestroy");
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid)
                .child(FirebaseDatabaseInterface.PLANINFO_NODE)
                .removeEventListener(mValueEventListener);
    }

}
