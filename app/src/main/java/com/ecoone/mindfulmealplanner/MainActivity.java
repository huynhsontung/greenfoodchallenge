package com.ecoone.mindfulmealplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ecoone.mindfulmealplanner.dashboard.DashboardFragment;
import com.ecoone.mindfulmealplanner.pledge.MyPledgeFragment;
import com.ecoone.mindfulmealplanner.pledge.PledgeFragment;
import com.ecoone.mindfulmealplanner.profile.ProfileFragment;
import com.ecoone.mindfulmealplanner.tools.NonSwipeableViewPager;


public class MainActivity extends AppCompatActivity implements
        ProfileFragment.OnDataPassingListener, BottomNavigationView.OnNavigationItemSelectedListener{

//    private String userDisplayName;
//    private String userEmail;
//    private Toolbar mToolbar;
//    private DrawerLayout mDrawer;
//    private ActionBarDrawerToggle mToggle;
//    private NavigationView mNavigationView;
//    private FirebaseUser firebaseUser;
//
//    private ImageView navUserIcon;
//    private View headerView;
//
//    final DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
//    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private NonSwipeableViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private android.support.v7.widget.Toolbar mToolbar;
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MainActivity)";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.main_bottom_nav);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        MenuItem menuItem = mBottomNavigationView.getMenu().findItem(R.id.nav_dashboard);
        setTitle(menuItem.getTitle());
        setupFragmentListForNav();
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setupFragmentListForNav() {
        mViewPager = findViewById(R.id.main_content);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0: return DashboardFragment.newInstance();

                    case 1: return PledgeFragment.newInstance();

//                    case 2: return null; // meal tracker fragment

                    default: return ProfileFragment.newInstance();
                }
            }
            @Override
            public int getCount() {
                return mBottomNavigationView.getMenu().size();
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

//    private void switchFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.replace(R.id.main_content, fragment);
//        ft.commit();
//    }

    @Override
    public void passDataFromProfileToMain(int input) {
//        Log.i(TAG, CLASSTAG + "onLogout: got the input " + input);
//
//        if (input == 1) {
//            FirebaseDatabaseInterface.deleteUserData();
//        }
//        AuthUI.getInstance().signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent= new Intent(MainActivity.this, InitialSetupActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
    }



//    private static final int LOGOUT_SIGN = 0;
//
//    public static Intent newIntent(Context packageContext) {
//        Intent intent = new Intent(packageContext, MainActivity.class);
////        intent.putExtra(EXTRA_USERNAME, username);
//        return intent;


//    private ShareActionProvider mShareActionProvider;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        mToolbar = findViewById(R.id.toolbar);
//        mDrawer = findViewById(R.id.drawer_layout);
//
//        userDisplayName = firebaseUser.getDisplayName();
//        userEmail = firebaseUser.getEmail();
//
//        mNavigationView = findViewById(R.id.nav_view);
//        mNavigationView.setNavigationItemSelectedListener(this);
//        headerView = mNavigationView.getHeaderView(0);
//        navUserIcon = headerView.findViewById(R.id.nav_user_icon);
//
//        setUserIcon();
//        setupNavigationDrawer();
//        showDashboard();
//    }
//
//    private void setUserIcon() {
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.child("userInfo").getValue(User.class);
//
//                if (user != null) {
//                    String mIconName = user.iconName;
//                    navUserIcon.setImageResource(getDrawableIdbyName(mIconName));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
//                .addValueEventListener(valueEventListener);
//    }
//

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        setTitle(menuItem.getTitle());
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard :
                mViewPager.setCurrentItem(0);
                break;

            case R.id.nav_pledge :
                mViewPager.setCurrentItem(1);
                break;

            case R.id.nav_profile :
                mViewPager.setCurrentItem(3);
                break;

//            case R.id.nav_meal_tracker :
//                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(
                        android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareText = "I have pledged ";
                shareText = shareText + String.valueOf(MyPledgeFragment.pledgeAmount) +"kg of CO2e in this app. Come and take a look!";
                intent.putExtra(

                        android.content.Intent.EXTRA_TEXT, shareText+"     Download this app here https://play.google.com/store/search?q=greenfoodchallenge");

                startActivity(Intent.createChooser(
                        intent,
                        "Share Via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //
//    private void setupNavigationDrawer() {
//        setSupportActionBar(mToolbar);
//        mToggle = new ActionBarDrawerToggle(
//                this, mDrawer, mToolbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        mDrawer.addDrawerListener(mToggle);
//        mToggle.syncState();
//
//        // Setup icon and name
//
//        TextView navUsernameText = headerView.findViewById(R.id.nav_username);
//        TextView navDisplayNameText = headerView.findViewById(R.id.nav_display_name);
//        navDisplayNameText.setText(userDisplayName);
//        navUsernameText.setText(userEmail);
//
//        navUserIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getSupportFragmentManager();
//                UserIconDialogFragment dialog= UserIconDialogFragment.newInstance();
//                dialog.show(fm, "fragment_icon");
//            }
//        });
////        Transformation circularTransform = new Transformation() {
////            @Override
////            public Bitmap transform(Bitmap source) {
////                final int margin = 0;
////                final int radius = 50;
////                final Paint paint = new Paint();
////                paint.setAntiAlias(true);
////                paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
////                        Shader.TileMode.CLAMP));
////
////                Bitmap output = Bitmap.createBitmap(source.getWidth(),
////                        source.getHeight(), Bitmap.Config.ARGB_8888);
////                Canvas canvas = new Canvas(output);
////                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
////                        - margin, source.getHeight() - margin), radius, radius, paint);
////
////                if (source != output) {
////                    source.recycle();
////                }
////
////                return output;
////            }
////
////            @Override
////            public String key() {
////                return "rounded";
////            }
////        };
////        Picasso.get()
////                .load(firebaseUser.getPhotoUrl())
////                .resize(200,200)
////                .transform(circularTransform)
////                .into(navUserIcon);
//    }
//
//    private void showDashboard() {
//        Menu menu = mNavigationView.getMenu();
//        MenuItem menuItem = menu.findItem(R.id.fragment_dashboard_plan);
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
//        switchFragment(new DashboardPlanFragment());
//    }
//

//
//    private int getDrawableIdbyName(String name) {
//        int resourceId = getResources().getIdentifier(name, "drawable", getPackageName());
//        return resourceId;
//    }
//
//    @Override
//    public void onBackPressed() {
//        Log.i(TAG, CLASSTAG + "backpressed");
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////        Log.i("test", "Item:" +item);
//        invalidateOptionsMenu();
//        Fragment fragment = null;
//        // Handle navigation view item clicks here..
//        int id = item.getItemId();
//
//        if (id == R.id.fragment_dashboard_plan) {
//            fragment = new DashboardPlanFragment();
//
//        }
//        else if (id == R.id.fragment_plan_list) {
//            fragment = new PlanListFragment();
//        }
//
//        else if (id == R.id.fragment_pledge) {
//            fragment = new PledgeFragment();
//        }
//
//        else if (id == R.id.fragment_settings) {
//            //..
//            Intent intent =new  Intent(this,SettingsActivity.class);
////            Bundle bundle = new Bundle();
////            bundle.putString("VALUE_SEND","Settings");
////            intent.putExtras(bundle);
//            startActivityForResult(intent, LOGOUT_SIGN);
//        }
//
//        if (fragment != null) {
//            switchFragment(fragment);
//        }
//        if(id != R.id.fragment_settings)
//            setTitle(item.getTitle());
//        else {
//            item.setCheckable(false);
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//

//
//    @Override
//    public void onLogout(int input) {
//        Log.i(TAG, "onLogout: got the input: " + input + CLASSTAG);
//        navUserIcon.setImageResource(input);
//
//        String iconName = getResources().getResourceEntryName(input);
//        FirebaseDatabaseInterface.updateUserIconName(iconName);
//    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, CLASSTAG + " onResume");
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
    }


}
