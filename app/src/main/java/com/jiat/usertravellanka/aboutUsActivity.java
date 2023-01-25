package com.jiat.usertravellanka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class aboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = findViewById(R.id.imageView10);
        Glide.with(this)
                .load(R.drawable.logo)
                .override(80,80)
                .into(imageView);

//        ImageView backimg = findViewById(R.id.imageView11);
//        Glide.with(getApplicationContext()).load(R.drawable.previous).override(39,39).into(backimg);
//        findViewById(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(aboutUsActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}