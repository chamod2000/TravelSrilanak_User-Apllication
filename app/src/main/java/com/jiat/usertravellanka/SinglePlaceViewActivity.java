package com.jiat.usertravellanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SinglePlaceViewActivity extends AppCompatActivity {


    private static final String TAG = "SingleView";
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    Place place;
    public static String id;
    public double lati;
    public double logi;
    public String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place_view);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        String lName = intent.getStringExtra("locationName");

        Log.i(TAG,lName);

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


        firestore.collection("Place/Wildlife/dWildlife")
                .whereEqualTo("pname",lName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                      place = snapshot.toObject(Place.class);


                     id = snapshot.getId();


//                    lati =place.getLatitude();
//                    String latitu = String.valueOf(lati);
//                    pLatitude.setText(latitu);
//
//                    lati =place.getLongitude();
//                    String logi = String.valueOf(lati);
//                    pLongitute.setText(logi);


                    placeName.setText(place.getPname());
                    placeDescription.setText(place.getDetails());
                    placeLocationAdd.setText(place.getPlaceLocation());
                    placeContactNu.setText(place.getTell());
                    placeOpenTime.setText(place.getOpenClose());
                    placeWebsite.setText(place.getWebsite());

                    img = place.getImage();
                    Log.i(TAG,img);


                 }
             }
        });


//        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //firestore.collection("Place/Wildlife/dWildlife")
//
//                TextView placeName = findViewById(R.id.editTextTextPlaceName);
//                TextView placeDescription = findViewById(R.id.textViewdescription);
//                TextView placeLocationAdd = findViewById(R.id.editTextTextplaceLocation);
//                TextView placeContactNu = findViewById(R.id.editTextTextContactNumber);
//                TextView placeOpenTime= findViewById(R.id.editTextTextOpenClose);
//                TextView placeWebsite= findViewById(R.id.editTextTextWebsitename);
//
//                String pName = placeName.getText().toString();
//                String pDescription = placeDescription.getText().toString();
//                String pPlaceLocation = placeLocationAdd.getText().toString();
//                String pContact = placeContactNu.getText().toString();
//                String pOpentime = placeOpenTime.getText().toString();
//                String pWebsite = placeWebsite.getText().toString();
//
//                double longi = place.getLongitude();
//                double latitute = place.getLongitude();
////                img = place.getImage();
////                Log.i(TAG,img);
//
//            //   Place place = new Place(pName,pDescription,latitute,longi,img,pPlaceLocation,pContact,pOpentime,pWebsite);
//
//
//            }
//        });


        String photo = img;

        storage.getReference("location-images/"+photo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.i(TAG,uri.getPath());

                Glide.with(getApplicationContext()).load(uri).into(image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,e.getMessage());
            }
        });




    }
}