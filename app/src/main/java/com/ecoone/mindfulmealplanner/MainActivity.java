package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.fragments.DashboardFragment;
import com.ecoone.mindfulmealplanner.fragments.PlanListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String mUsername;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private FirebaseUser firebaseUser;

    public static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.mainactivity.username";

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MainActivity)";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainActivity.class);
//        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);

        mUsername = firebaseUser.getEmail();
//        Log.i(TAG, "Username: " + mUsername + CLASSTAG);


        setupNavigationDrawer();
        showDashboard();
    }

    private void setupNavigationDrawer() {



        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Setup icon and name
        View headerView = mNavigationView.getHeaderView(0);
        TextView navUsernameText = headerView.findViewById(R.id.nav_username);
        TextView navDisplayNameText = headerView.findViewById(R.id.nav_display_name);
        navDisplayNameText.setText(firebaseUser.getDisplayName());
        navUsernameText.setText(firebaseUser.getEmail());
        ImageView navUserIcon = headerView.findViewById(R.id.nav_user_icon);
        Transformation circularTransform = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                final int margin = 0;
                final int radius = 50;
                final Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP));

                Bitmap output = Bitmap.createBitmap(source.getWidth(),
                        source.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                        - margin, source.getHeight() - margin), radius, radius, paint);

                if (source != output) {
                    source.recycle();
                }

                return output;
            }

            @Override
            public String key() {
                return "rounded";
            }
        };
        Picasso.get()
                .load(firebaseUser.getPhotoUrl())
                .resize(200,200)
                .transform(circularTransform)
                .into(navUserIcon);
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
