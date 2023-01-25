package com.jiat.usertravellanka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.maps.android.PolyUtil;
import com.jiat.usertravellanka.model.Place;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class maplocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1312;
    private GoogleMap map;
    private Marker mymarker;
    private Marker shopMarker;
    String latitude;
    String longitude;
    FirebaseFirestore firestore;
    String imf;
    String imf2;
    private static final String TAG ="loca";
    LatLng latLng;
    private int moveEnable =0;
    private CollectionReference shopOwners;
    private Polyline polyline;
    private Marker marker_me,marker_pin;

    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maplocation);

        Intent intent = getIntent();
        String lName = intent.getStringExtra("locationName");

        Intent intent2 = getIntent();
        String lName2 = intent2.getStringExtra("location");

        if (lName != null && lName2 == null){
            path = "Place/Wildlife/dWildlife";
            imf = singleLocationViewActivity.loca;
        }else if (lName2 != null && lName == null){
            path = "Place/Culture/dCulture";
            imf = cultureSingleView.cloca;
        }

        Log.i(TAG,"Path :"+path);

        firestore = FirebaseFirestore.getInstance();

//        imf = singleLocationViewActivity.loca;
//        Log.i(TAG,imf);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        firestore.collection(path)
                .whereEqualTo("pname",imf)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot snapshot : task.getResult()){
                    Place doc=snapshot.toObject(Place.class);
                    String tooklatitude=String.valueOf(doc.getLatitude());
                    String tooklongitude=String.valueOf(doc.getLongitude());

                    latitude=tooklatitude;
                    longitude=tooklongitude;
                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                    MarkerOptions markerOptionsShop = new MarkerOptions();
                    markerOptionsShop.icon(BitmapDescriptorFactory.fromResource(R.drawable.flagk));
                    markerOptionsShop.title(imf);
                    markerOptionsShop.position(latLng);
                    shopMarker=map.addMarker(markerOptionsShop);

                    CameraPosition position = CameraPosition.builder()
                            .target(latLng)
                            .zoom(15f)
                            .build();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
                    map.animateCamera(cameraUpdate);

                }}
        });

    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        map = googleMap;
        // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (checkPermissions()){
            map.setMyLocationEnabled(true);
        }else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }

        //taking the current location->START
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(5000)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) { super.onLocationAvailability(locationAvailability); }

                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        Location lastLocation = locationResult.getLastLocation();
                        LatLng latLng2 = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                        if(marker_me == null) {
                            MarkerOptions markerOptionsMe = new MarkerOptions();
                            markerOptionsMe.title("ME");
                            markerOptionsMe.position(latLng2);
                            marker_me = map.addMarker(markerOptionsMe);
                        }else{
                            marker_me.setPosition(latLng2);
                        }
                        if(moveEnable == 0){
                            moveCamera(latLng2);
                            moveEnable++;
                        }
                    }
                }, Looper.getMainLooper());

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                getDirections(marker_me.getPosition(),marker.getPosition());
                return false;
            }
        });
    }


    public void getDirections(LatLng start,LatLng end){
        OkHttpClient client = new OkHttpClient();
        String URL="https://maps.googleapis.com/maps/api/directions/json?origin="
                +start.latitude
                +","
                +start.longitude
                +"&destination="
                +end.latitude
                +","
                +end.longitude
                +"&key="
                +getString(R.string.direction_api_key);

        Log.i(TAG,URL);

        Request request = new Request.Builder().url(URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();

                try{
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray routes = jsonObject.getJSONArray("routes");
                    Log.i(TAG,routes.length()+" ");


                    JSONObject route = routes.getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    Log.i(TAG,overviewPolyline.toString());

                    List<LatLng> points = PolyUtil.decode(overviewPolyline.getString("points"));


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(polyline==null){

                                PolylineOptions polylineOptions = new PolylineOptions();
                                polylineOptions.width(15);
                                polylineOptions.color(getColor(R.color.color_road));
                                polylineOptions.addAll(points);
                                polyline = map.addPolyline(polylineOptions);

                            }else {
                                polyline.setPoints(points);
                            }
                        }
                    });


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        });
    }




    public void moveCamera(LatLng latLng2){

        CameraPosition position = CameraPosition.builder()
                .target(latLng2)
                .zoom(10f)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
        map.animateCamera(cameraUpdate);
    }



    //Checking whether the permissions are allowed ->START
    @SuppressLint("NewApi")
    public boolean checkPermissions(){
        boolean permissions = false;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            permissions = true;
        }
        return permissions;
    }
//Checking whether the permissions are allowed ->END



    //checking whether the requested permission were allowed ->START
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionsGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){

            for (int i = 0; i < permissions.length; i++){

                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    permissionsGranted = true;

                }else if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    permissionsGranted = true;
                }
            }

            if (permissionsGranted){
                map.setMyLocationEnabled(true);
            }
        }
    }
    //checking whether the requested permission were allowed ->END
}