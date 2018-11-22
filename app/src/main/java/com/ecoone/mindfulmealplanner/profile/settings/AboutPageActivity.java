package com.ecoone.mindfulmealplanner.profile.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ecoone.mindfulmealplanner.R;

//...
public class AboutPageActivity extends AppCompatActivity {

    private String[] titles = {"What is a Foodprint?", "The Green Food Challenge", "Working as a Community"};
    private String[] descriptions = {"Food’s carbon footprint, or footprint, is the greenhouse gas " +
            "emissions produced by growing, rearing, farming, processing, transporting, storing, " +
            "cooking and disposing of the food you eat. In the US, each household produces 48 tons of greenhouse gases. Transport, housing and food have the three largest carbon footprints. Food produces about 8 tons " +
            "of emissions per household, or about 17% of the total. " +
            "Worldwide, new reports suggest that livestock agriculture produces " +
            "around a half of all man-made emissions.",

            "We wish to raise awareness and promote behaviour change. Through this application, " +
                    "participants will learn how much their diet produces in terms of CO2e, and " +
                    "discover how much CO2e they could save by making dietary adjustments.",

            "Over the course of the challenge, participants will track their Green Meals. They will " +
                    "be able to see how many tonnes of CO2e they save as a community, as well as share" +
                    "meals and restaurants that offer low carbon meals." +
                    " By incentivizing these meals," +
                    "it is hoped that participants learn to sustainably change their eating habits" +
                    "towards a lighter footprint."
            };
    private int[] images = {R.drawable.foodprint, R.drawable.veggiesandstuff, R.drawable.forest};
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about2);
        mToolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("About");

        recyclerView = findViewById(R.id.recycler_view_123);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        adapter = new RecyclerAdapter(titles, descriptions, images);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
