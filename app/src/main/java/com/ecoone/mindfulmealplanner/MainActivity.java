package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.fragments.DashboardFragment;
import com.ecoone.mindfulmealplanner.fragments.PlanListFragment;
import com.ecoone.mindfulmealplanner.fragments.PledgeFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String mUsername;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;


    public static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.mainactivity.username";

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MainActivity)";

    public static Intent newIntent(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
//        Log.i(TAG, "Username: " + mUsername + CLASSTAG);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.nav_username);
        nav_user.setText(mUsername);
        setSidebarAction();
        showDashboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.mShare:
                Intent i = new Intent(
                        android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(
                        android.content.Intent.EXTRA_TEXT, "let's reduce carbon footprint https://play.google.com/store/search?q=green%20food%20challenge");
                startActivity(Intent.createChooser(
                        i,
                        "Share Via"));
                break;
        }
        Toast.makeText(getApplicationContext(), "You click on menu share", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void setSidebarAction() {
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void showDashboard() {
        Menu menu = mNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.fragment_dashboard);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        switchFragment(new DashboardFragment());
    }

    private void switchFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERNAME, mUsername);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, fragment);
        ft.commit();
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Log.i("test", "Item:" +item);
        Fragment fragment = null;
        // Handle navigation view item clicks here..
        int id = item.getItemId();

        if (id == R.id.fragment_dashboard) {
            fragment = new DashboardFragment();

        }

        else if (id == R.id.fragment_pledge) {
            fragment = new PledgeFragment();
        }

        else if (id == R.id.fragment_plan_list) {
            fragment = new PlanListFragment();
        }

        else if (id == R.id.fragment_settings) {
            //..
            Intent intent =new  Intent(this,SettingsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("VALUE_SEND","Settings");
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (fragment != null) {
            switchFragment(fragment);
        }
        if(id != R.id.fragment_settings)
            setTitle(item.getTitle());
        else {
            item.setCheckable(false);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
