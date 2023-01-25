package com.jiat.usertravellanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class testingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NavigationBarView.OnItemSelectedListener

{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);
        toolbar =findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_frawer_open,R.string.navigation_frawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

//        View headerView  =   navigationView.getHeaderView(0);
//
//       ImageView imageView = headerView.findViewById(R.id.profilePic);
//        Glide.with(this)
//                .load(R.drawable.ic_baseline_person_pin_24)
//                .circleCrop()
//                .override(100,100)
//                .into(imageView);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
    }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       // Toast.makeText(this,"CLick", Toast.LENGTH_SHORT).show();

        switch (item.getItemId()){
            case R.id.sideAbout:

                Intent intent = new Intent(testingActivity.this,MainActivity.class);
                startActivity(intent);

                break;
            case R.id.sidelogout:

                firebaseAuth = FirebaseAuth.getInstance();

                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(testingActivity.this,loginUserActivity.class);
                startActivity(intent1);

                break;
        }

        return true;
    }
}