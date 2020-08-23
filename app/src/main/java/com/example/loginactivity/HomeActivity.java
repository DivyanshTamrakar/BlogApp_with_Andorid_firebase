package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    View hview;
    public TextView user;
    public TextView ema;
    public CircularImageView circle;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    public String user_name;
    public String user_email;
    public String current_user;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        change();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


// -------------------- Toolbar Secton ----------------------//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("All Blogs");
        toolbar.setTitleTextColor(Color.WHITE);



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hview = navigationView.getHeaderView(0);

        circle = hview.findViewById(R.id.circle_img);
        ema = (TextView) hview.findViewById(R.id.akash);
        user = hview.findViewById(R.id.dibbi);


        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AllBlogFragment()).commit();*/
        navigationView.setCheckedItem(R.id.nav_allBlog);




        bottomNavigationView = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Tab1Fragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        fragment = new Tab1Fragment();
                        break;

                    case R.id.create:
                        fragment = new Tab2Fragment();
                        break;

                    case R.id.account:
                        fragment = new Tab3Fragment();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = mAuth.getCurrentUser();

        if (current_user == null) {

            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
        else{

            String current = current_user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String display_name = dataSnapshot.child("username").getValue(String.class);
                    String display_email = dataSnapshot.child("email").getValue(String.class);
                    String display_image = dataSnapshot.child("image").getValue(String.class);
                    user.setText(display_name);
                    ema.setText(display_email);

                    if (!display_image.equals("default")) {
                        Picasso.get().load(display_image).into(circle);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_allBlog:
               /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllBlogFragment()).commit();*/
                break;
            case R.id.nav_newBlog:
             /*   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NewBlog()).commit();*/
                break;
            case R.id.nav_myblog:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyBlog()).commit();
                break;

            case R.id.nav_editBlog:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditBlogFragment()).commit();
                break;
            case R.id.nav_deleteBlog:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DeleteBlogFragment()).commit();
                break;
            case R.id.nav_shareblog:
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(HomeActivity.this).
                        setTitle("Are you sure ? ").
                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
                mAuth.signOut();
                break;
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                  //      new ProfileFragment()).commit();
                break;


        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }
    private void change() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.black));
    }
}
