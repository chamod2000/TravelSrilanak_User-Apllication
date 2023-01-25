package com.jiat.usertravellanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.jiat.usertravellanka.model.Place;

public class cultureSingleView extends AppCompatActivity {

    private static final String TAG = "SingleView";
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    Place place;
    public static String id;
    public double lati;
    public double logi;
    public String img;
    public static String cloca;
    public static String cpcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_single_view);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ImageView backimg = findViewById(R.id.imageView9);
//        Glide.with(getApplicationContext()).load(R.drawable.previous).override(39,39).into(backimg);
//        findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(cultureSingleView.this,cultureActivity.class);
//                startActivity(intent);
//            }
//        });

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        String clName = intent.getStringExtra("locationName");
        String cplaceCategory = intent.getStringExtra("categoty");
        cloca = clName;
        cpcategory = cplaceCategory;
        Log.i(TAG,clName);

        place =new Place();


        TextView placeName = findViewById(R.id.editTextTextPlaceNameC);
        TextView placeDescription = findViewById(R.id.textViewdescriptionC);
        TextView placeLocationAdd = findViewById(R.id.editTextTextplaceLocationC);
        TextView placeContactNu = findViewById(R.id.editTextTextContactNumberC);
        TextView placeOpenTime= findViewById(R.id.editTextTextOpenCloseC);
        TextView placeWebsite= findViewById(R.id.editTextTextWebsitenameC);
//        TextView pLatitude= findViewById(R.id.latitude);
//        TextView pLongitute= findViewById(R.id.longitude);
        ImageView image = findViewById(R.id.imageViewC);


        firestore.collection("Place/Culture/dCulture")
                .whereEqualTo("pname",clName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    place = snapshot.toObject(Place.class);
                    id = snapshot.getId();

                    placeName.setText(place.getPname());
                    placeDescription.setText(place.getDetails());
                    placeLocationAdd.setText(place.getPlaceLocation());
                    placeContactNu.setText(place.getTell());
                    placeOpenTime.setText(place.getOpenClose());
                    placeWebsite.setText(place.getWebsite());

                    img = place.getImage();
                    Log.i(TAG,img);
                    tookimg(img);


                }
            }
        });



        findViewById(R.id.buttonmapC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(cultureSingleView.this,maplocationActivity.class);
                intent1.putExtra("location",cloca);
                startActivity(intent1);
            }
        });


        ImageView nearby = findViewById(R.id.imageView81);
        Glide.with(getApplicationContext()).load(R.drawable.destinations).override(50,50).into(nearby);
        findViewById(R.id.imageView81).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cultureSingleView.this,naerbylocation.class);
//                intent.putExtra("latitude",lati);
//                intent.putExtra("longitude",logi);
                startActivity(intent);


            }
        });


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

    public void tookimg(String img){


        storage.getReference("location-images/"+img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.i(TAG, uri.getPath());
                ImageView image = findViewById(R.id.imageViewC);
                Glide.with(getApplicationContext()).load(uri).into(image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getMessage());
            }
        });
    }
}