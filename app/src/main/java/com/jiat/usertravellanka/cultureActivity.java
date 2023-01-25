package com.jiat.usertravellanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.jiat.usertravellanka.model.Place;

import java.util.ArrayList;

public class cultureActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ArrayList<Place> places;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture);

        getSupportActionBar().setTitle("Culture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        places =new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.culturerecycleview);
        Cultureadapter adapter = new Cultureadapter(places,getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation()));
        recyclerView.setAdapter(adapter);


        firestore.collection("Place/Culture/dCulture").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    Place place = snapshot.toObject(Place.class);
                    places.add(place);
                }
                adapter.notifyDataSetChanged();

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

}