package com.jiat.usertravellanka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;

public class naerbylocation extends AppCompatActivity {

    Spinner spType;
    Button btfind;
    SupportMapFragment supportMapFragment;
    GoogleMap map3;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    double lati;
    double logi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naerbylocation);

//         Intent intent = getIntent();
//         String lt =intent.getStringExtra("latitude");
//         String lng = intent.getStringExtra("longitude");

//        Log.i(TAG,lt+" "+lng);
//
//         lati = Double.parseDouble(lt);
//         logi = Double.parseDouble(lng);



        spType = findViewById(R.id.sp_type);
        btfind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        String[] placeTypeList = {"atm", "bank", "hospital", "movie_theater", "restaurant"};
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Movie Theater", "Restaurant"};

        spType.setAdapter(new ArrayAdapter<>(naerbylocation.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, placeNameList));


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(naerbylocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else {
            ActivityCompat.requestPermissions(naerbylocation.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        btfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int  i  = spType.getSelectedItemPosition();
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + currentLat + "," + currentLong + "&radius=5000" + "&type=" + placeTypeList[i] + "&sensor=true" + "&key=" + getResources().getString(R.string.google_map_key);

                new PlaceTask().execute(url);

            }
        });


    }




    private void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
//                    currentLat =lati;
//                    currentLong =logi;

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map3 = googleMap;

                            map3.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLong),10));
                        }
                    });
                }
            }
        });


    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            }
        }
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data =downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);

            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {


        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line ="";

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        return data;

    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>>{


        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser =new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object;

            try {
                object =new JSONObject(strings[0]);

                mapList = jsonParser.parseResult(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
//            super.onPostExecute(hashMaps);
            map3.clear();

            for (int i=1;i < hashMaps.size();i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name  = hashMapList.get("name");

                LatLng latLng =  new LatLng(lat,lng);
                MarkerOptions options = new MarkerOptions();

                options.position(latLng);
                options.title(name);
                map3.addMarker(options);

            }
        }
    }



}