package com.ecoone.mindfulmealplanner.profile.settings;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ecoone.mindfulmealplanner.R;
import com.firebase.ui.auth.data.model.Resource;

//...
public class AboutPageActivity extends AppCompatActivity {

    private String[] titles;
    private String[] descriptions;
    private int[] images = {R.drawable.foodprint, R.drawable.veggiesandstuff, R.drawable.forest, R.drawable.stanley_park};
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about2);
        mToolbar = findViewById(R.id.toolbar2);

        Resources resources = getResources();
        titles = resources.getStringArray(R.array.titles_for_about);
        descriptions = resources.getStringArray(R.array.descriptions_about);

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
