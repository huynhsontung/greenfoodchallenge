package com.ecoone.mindfulmealplanner;

import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Plan curplan=new Plan();
    float beefco2e = curplan.getBeef() * 27/1000;
    float porkco2e = curplan.getPork()* 12/1000;
    float chickenco2e = curplan.getChicken()*7/1000;
    float fishco2e = curplan.getFish()*6/1000;
    float eggsco2e = curplan.getEggs()*5/1000;
    float beansco2e = curplan.getBeans()*2/1000;
    float vegetablesco2e = curplan.getVegetables()*2/1000;

    float sumco2e = beefco2e+porkco2e+chickenco2e+fishco2e+eggsco2e+beansco2e+vegetablesco2e;

    float beefco2per = beefco2e/sumco2e;
    float porkco2per = porkco2e/sumco2e;
    float chickenco2per = chickenco2e/sumco2e;
    float fishco2per = fishco2e/sumco2e;
    float eggsco2per = eggsco2e/sumco2e;
    float beansco2per = beansco2e/sumco2e;
    float vegetablesco2per = vegetablesco2e/sumco2e;


    float beefgram = curplan.getBeef();
    float porkgram = curplan.getPork();
    float chickengram = curplan.getChicken();
    float fishgram = curplan.getFish();
    float eggsgram = curplan.getEggs();
    float beansgram = curplan.getBeans();
    float vegetablesgram = curplan.getVegetables();

    float sumgram = beefgram+porkgram+chickengram+fishgram+eggsgram+beansgram+vegetablesgram;

    float beefgramper = beefgram/sumgram;
    float porkgramper = porkgram/sumgram;
    float chickengramper = chickengram/sumgram;
    float fishgramper = fishgram/sumgram;
    float eggsgramper = eggsgram/sumgram;
    float beansgramper = beansgram/sumgram;
    float vegetablesgramper = vegetablesgram/sumgram;



    float gramper[] ={ beefgramper, porkgramper, chickengramper, fishgramper , eggsgramper, beansgramper, vegetablesgramper};
    String foodnames[] = {"beef", "pork", "chicken", "fish", "eggs", "beans", "vegetables"};
    float co2per[] = {beefco2per,porkco2per, chickenco2per,fishco2per,eggsco2per,beansco2per,vegetablesco2per};






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

        setupImproButton();
        setupPieChart1();
        setupPieChart2();
    }

    private void setupImproButton(){
        //Wire up the button to improve the plan
        //...get the button
        Button impro = (Button) findViewById(R.id.imp);
        impro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(Dashboard.this, "Clicked 'improve'.", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private void setupPieChart1(){
        //setup piechart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<gramper.length;i++){
            pieEntries.add(new PieEntry(gramper[i], foodnames[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntries,"Current grams");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);

        PieChart chart1=(PieChart) findViewById(R.id.PieChart1);
        chart1.setData(data);
        chart1.animateY(1000);
        chart1.invalidate();
    }


    private void setupPieChart2(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<co2per.length;i++){
            pieEntries.add(new PieEntry(co2per[i], foodnames[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntries,"Current co2e");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);

        PieChart chart2=(PieChart) findViewById(R.id.PieChart2);
        chart2.setData(data);
        chart2.animateY(1000);
        chart2.invalidate();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
