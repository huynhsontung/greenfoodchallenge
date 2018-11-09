package com.ecoone.mindfulmealplanner;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.User;
import com.ecoone.mindfulmealplanner.InitialSetup.InitialSetupActivity;
import com.ecoone.mindfulmealplanner.InitialSetup.InitialSetupViewModel;
import com.ecoone.mindfulmealplanner.PlanList.PlanListFragment;
import com.ecoone.mindfulmealplanner.Pledge.MyPledgeFragment;
import com.ecoone.mindfulmealplanner.UserIconDialogFragment.OnInputListener;
import com.ecoone.mindfulmealplanner.DashBoard.DashboardFragment;
import com.ecoone.mindfulmealplanner.Setting.SettingsActivity;
import com.ecoone.mindfulmealplanner.Pledge.PledgeFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnInputListener {

    private String userDisplayName;
    private String userEmail;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private FirebaseUser firebaseUser;

    private ImageView navUserIcon;
    private View headerView;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MainActivity)";
    private static final int LOGOUT_SIGN = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainActivity.class);
//        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);

        userDisplayName = firebaseUser.getDisplayName();
        userEmail = firebaseUser.getEmail();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        headerView = mNavigationView.getHeaderView(0);
        navUserIcon = headerView.findViewById(R.id.nav_user_icon);

        setUserIcon();
        setupNavigationDrawer();
        showDashboard();
    }

    private void setUserIcon() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("userInfo").getValue(User.class);

                if (user != null) {
                    String mIconName = user.iconName;
                    navUserIcon.setImageResource(getDrawableIdbyName(mIconName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
                .addValueEventListener(valueEventListener);
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
                String shareText = "I have pledged ";
                shareText = shareText + String.valueOf(MyPledgeFragment.pledgeAmount) +"kg of CO2e in this app. Come and take a look!";
                i.putExtra(

                        android.content.Intent.EXTRA_TEXT, shareText+"     Download this app here https://play.google.com/store/search?q=greenfoodchallenge");

                startActivity(Intent.createChooser(
                        i,
                        "Share Via"));
                break;
        }
//        Toast.makeText(getApplicationContext(), "You click on menu share", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawer() {
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        // Setup icon and name

        TextView navUsernameText = headerView.findViewById(R.id.nav_username);
        TextView navDisplayNameText = headerView.findViewById(R.id.nav_display_name);
        navDisplayNameText.setText(userDisplayName);
        navUsernameText.setText(userEmail);

        navUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                UserIconDialogFragment dialog= UserIconDialogFragment.newInstance();
                dialog.show(fm, "fragment_icon");
            }
        });
//        Transformation circularTransform = new Transformation() {
//            @Override
//            public Bitmap transform(Bitmap source) {
//                final int margin = 0;
//                final int radius = 50;
//                final Paint paint = new Paint();
//                paint.setAntiAlias(true);
//                paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
//                        Shader.TileMode.CLAMP));
//
//                Bitmap output = Bitmap.createBitmap(source.getWidth(),
//                        source.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(output);
//                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
//                        - margin, source.getHeight() - margin), radius, radius, paint);
//
//                if (source != output) {
//                    source.recycle();
//                }
//
//                return output;
//            }
//
//            @Override
//            public String key() {
//                return "rounded";
//            }
//        };
//        Picasso.get()
//                .load(firebaseUser.getPhotoUrl())
//                .resize(200,200)
//                .transform(circularTransform)
//                .into(navUserIcon);
    }

    private void showDashboard() {
        Menu menu = mNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.fragment_dashboard);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        switchFragment(new DashboardFragment());
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, fragment);
        ft.commit();
    }

    private int getDrawableIdbyName(String name) {
        int resourceId = getResources().getIdentifier(name, "drawable", getPackageName());
        return resourceId;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, CLASSTAG + "backpressed");
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
        invalidateOptionsMenu();
        Fragment fragment = null;
        // Handle navigation view item clicks here..
        int id = item.getItemId();

        if (id == R.id.fragment_dashboard) {
            fragment = new DashboardFragment();

        }
        else if (id == R.id.fragment_plan_list) {
            fragment = new PlanListFragment();
        }

        else if (id == R.id.fragment_pledge) {
            fragment = new PledgeFragment();
        }

        else if (id == R.id.fragment_settings) {
            //..
            Intent intent =new  Intent(this,SettingsActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("VALUE_SEND","Settings");
//            intent.putExtras(bundle);
            startActivityForResult(intent, LOGOUT_SIGN);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == LOGOUT_SIGN) {
            if (data != null) {
                int logoutSign = SettingsActivity.logoutAction(data);
                if (logoutSign == 1) {
                    FirebaseDatabaseInterface.deleteUserData();
                }
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent= new Intent(MainActivity.this, InitialSetupActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

            }

        }
    }

    @Override
    public void sendInput(int input) {
        Log.i(TAG, "sendInput: got the input: " + input + CLASSTAG);
        navUserIcon.setImageResource(input);

        String iconName = getResources().getResourceEntryName(input);
        FirebaseDatabaseInterface.updateUserIconName(iconName);
    }

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
