package com.ecoone.mindfulmealplanner.fragments;

import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;

import java.util.List;

public class PlanListFragment extends Fragment {

    private String mUsername;
    private AppDatabase mDb;

    private RecyclerView mRecyclerView;
    private PlanAdapter mAdapter;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(PlanListFragment)";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_list, null );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // write your code here

        mDb = AppDatabase.getDatabase(getContext());
        DbInterface.setDb(mDb);

        mUsername = getArguments().getString(MainActivity.EXTRA_USERNAME);

        mRecyclerView = view.findViewById(R.id.plan_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        updateUI();
    }

    private void updateUI() {
        List<Plan> plans = DbInterface.getAllPlans(mUsername);

        mAdapter = new PlanAdapter(plans);
        mRecyclerView.setAdapter(mAdapter);

    }

    private class PlanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Plan mPlan;

        private TextView mPlanNameTextView;
        private TextView mPlanFoodAmountTextView;

        public PlanHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_plan, parent, false));
            itemView.setOnClickListener(this);

            mPlanNameTextView = itemView.findViewById(R.id.plan_name);
            mPlanFoodAmountTextView = itemView.findViewById(R.id.plan_food_amount);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mPlan.planName + " clicked!", Toast.LENGTH_SHORT).show();
        }

        public void bind(Plan plan) {
            mPlan = plan;
            mPlanNameTextView.setText(mPlan.planName);
            mPlanNameTextView.setTextColor(Color.BLACK);
            mPlanFoodAmountTextView.setText(DbInterface.getPlanDatatoString(plan));
        }
    }

    private class PlanAdapter extends RecyclerView.Adapter<PlanHolder> {

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
            planHolder.bind(plan);
        }

        @Override
        public int getItemCount() {
            return mPlans.size();
        }
    }

}
