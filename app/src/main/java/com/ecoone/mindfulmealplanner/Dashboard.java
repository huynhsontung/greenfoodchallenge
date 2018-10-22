package com.ecoone.mindfulmealplanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.loginactivity.username";

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        
            // setup database and sharedPreference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentUsername = sharedPref.getString(EXTRA_USERNAME, null);
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        String currentPlanName = db.userDao().getCurrentPlanName(currentUsername);
        Plan currentPlan = db.planDao().getPlanFromUser(currentUsername,currentPlanName);


        float beefInGram = currentPlan.beef;
        float porkInGram = currentPlan.pork;
        float chickenInGram = currentPlan.chicken;
        float fishInGram = currentPlan.fish;
        float eggsInGram = currentPlan.eggs;
        float beansInGram = currentPlan.beans;
        float vegetablesInGram = currentPlan.vegetables;
        
        float beefco2e = beefInGram * 27/1000;
        float porkco2e = porkInGram* 12/1000;
        float chickenco2e = chickenInGram*7/1000;
        float fishco2e = fishInGram*6/1000;
        float eggsco2e = eggsInGram*5/1000;
        float beansco2e = beansInGram*2/1000;
        float vegetablesco2e = vegetablesInGram*2/1000;

        float sumco2e = beefco2e+porkco2e+chickenco2e+fishco2e+eggsco2e+beansco2e+vegetablesco2e;

        float beefco2per = beefco2e/sumco2e;
        float porkco2per = porkco2e/sumco2e;
        float chickenco2per = chickenco2e/sumco2e;
        float fishco2per = fishco2e/sumco2e;
        float eggsco2per = eggsco2e/sumco2e;
        float beansco2per = beansco2e/sumco2e;
        float vegetablesco2per = vegetablesco2e/sumco2e;
        

        float sumInGram = beefInGram+porkInGram+chickenInGram+fishInGram+eggsInGram+beansInGram+vegetablesInGram;

        float beefPercentage = beefInGram/sumInGram;
        float porkPercentage = porkInGram/sumInGram;
        float chickenPercentage = chickenInGram/sumInGram;
        float fishPercentage = fishInGram/sumInGram;
        float eggsPercentage = eggsInGram/sumInGram;
        float beansPercentage = beansInGram/sumInGram;
        float vegetablesPercentage = vegetablesInGram/sumInGram;



        float percentage[] ={ beefPercentage, porkPercentage, chickenPercentage, fishPercentage , eggsPercentage, beansPercentage, vegetablesPercentage};
        String foodNames[] = {"beef", "pork", "chicken", "fish", "eggs", "beans", "vegetables"};
        float co2Percentage[] = {beefco2per,porkco2per, chickenco2per,fishco2per,eggsco2per,beansco2per,vegetablesco2per};

        setupImproveButton();
        setupPieChart1(percentage, foodNames);
        setupPieChart2(co2Percentage,foodNames);
    }

    private void setupImproveButton(){
        //Wire up the button to improve the plan
        //...get the button
        Button improveButton = findViewById(R.id.imp);
        improveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(Dashboard.this, "Clicked 'improve'.", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private void setupPieChart1(float[] percentage, String[] foodNames){
        //setup pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<percentage.length;i++){
            pieEntries.add(new PieEntry(percentage[i], foodNames[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntries,"Current grams");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);

        PieChart chart1=(PieChart) findViewById(R.id.PieChart1);
        chart1.setData(data);
        chart1.animateY(1000);
        chart1.invalidate();
    }


    private void setupPieChart2(float[] co2Percentage, String[] foodNames){
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<co2Percentage.length;i++){
            pieEntries.add(new PieEntry(co2Percentage[i], foodNames[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"Current co2e");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart2= findViewById(R.id.PieChart2);
        chart2.setData(data);
        chart2.animateY(1000);
        chart2.invalidate();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
