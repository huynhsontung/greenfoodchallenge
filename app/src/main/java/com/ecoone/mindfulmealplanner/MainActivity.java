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
import android.view.MenuItem;

import com.ecoone.mindfulmealplanner.fragments.DashboardFragment;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
//        Log.i(TAG, "Username: " + mUsername + CLASSTAG);

        setSidebarAction();
        showDashboard();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

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

        setTitle(item.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
