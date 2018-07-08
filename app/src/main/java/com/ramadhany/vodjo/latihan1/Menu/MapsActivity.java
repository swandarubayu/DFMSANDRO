package com.ramadhany.vodjo.latihan1.Menu;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramadhany.vodjo.latihan1.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    Button b1;
    private GoogleMap mMap;
    private DebugHelper hlp;
    private Location mlocation;
    double latitude, longitude;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                db.child("longitude").setValue(longitude);
                db.child("latitude").setValue(latitude).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MapsActivity.this,"data added",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        db= FirebaseDatabase.getInstance().getReference();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

//        LatLng center = new LatLng(41.385064,2.173403);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        hlp =  new DebugHelper();
        mMap.setOnCameraIdleListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

    }

        mMap.setMyLocationEnabled(true);
        //   mMap.setMinZoomPreference(6.0f);
        //  mMap.setMaxZoomPreference(14.0f);
        // LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Criteria criteria = new Criteria();
        //  Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        // mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, locationListener);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        longitude = myLocation.getLongitude();
        latitude = myLocation.getLatitude();
//db.child("longitude").setValue(myLocation.getLongitude());
        //      db.child("latitude").setValue(myLocation.getLatitude());

        String tot = new Double(latitude).toString();
        String ax = new Double(longitude).toString();
        String w = tot + "  " + ax;
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here..."));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //  Toast.makeText(MapsActivity.this, w, Toast.LENGTH_LONG).show();
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //db.child("longitude").setValue(location.getLongitude());
            //db.child("latitude").setValue(location.getLatitude());
            //        String tot = new Double(latitude).toString();
            //          String ax = new Double(longitude).toString();
//            String w = tot + "  " + ax;
            mMap.clear();
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here..."));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setMinZoomPreference(6.0f);
            mMap.setMaxZoomPreference(14.0f);

            //    Toast.makeText(MapsActivity.this, w, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        @Override
        public void onCameraIdle() {
            Projection projection = mMap.getProjection();
            double l1 = projection.getVisibleRegion().farLeft.longitude;
            double l2 = projection.getVisibleRegion().farRight.longitude;

            double grdSize = Math.abs(l2 - l1) / 8.0;

            hlp.drawDebugGrid(mMap, grdSize);
        }
    };
}
