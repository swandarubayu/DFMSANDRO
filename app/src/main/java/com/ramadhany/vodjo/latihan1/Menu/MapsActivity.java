package com.ramadhany.vodjo.latihan1.Menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.ramadhany.vodjo.latihan1.Model.GirdBody;
import com.ramadhany.vodjo.latihan1.Model.UserMovBody;
import com.ramadhany.vodjo.latihan1.R;
import com.ramadhany.vodjo.latihan1.helper.ApiService;
import com.ramadhany.vodjo.latihan1.helper.SQLiteHandler;
import com.ramadhany.vodjo.latihan1.helper.SessionManager;
import com.ramadhany.vodjo.latihan1.helper.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    Context mContext;
    ApiService mApiService;

    private SessionManager session;
    private SQLiteHandler db;

    private static final int MY_PERMISSIONS_REQUEST_LOCATIONS = 1;

    LocationManager locationManager ;
    boolean GpsStatus ;

    String koordinat[];
    String tepi[];
    LatLng batas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkGpsStatus();
            checkGpsStatus();
        }

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext().getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext().getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void checkPermission(){
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Permission already granted.",Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
    }

    private void requestPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkGpsStatus(){

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(GpsStatus == true) {
            Toast.makeText(getApplicationContext(),"Location Services Is Enable.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Location Services Is Disable.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getGird();

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLongitude(), location.getLatitude());
        MarkerOptions markerOptions = new MarkerOptions();

        String loc = "POINT(" + String.valueOf(location.getLongitude() + " " + location.getLatitude() + ")");
        Log.d("currentLoc", loc);

        if (location != null){
            kirimLoc(loc);

            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void kirimLoc(final String location) {
        HashMap<String, String> user = db.getUserDetails();

        String user_id = user.get("user_id");

        Date date = Calendar.getInstance().getTime();
        String now = date.getYear() + "-" + date.getMonth() + "-" + date.getDate() + " " + date.getHours() +  ":" + date.getMinutes() + ":" + date.getSeconds();

//        calander = Calendar.getInstance();
//        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        String updatedat = simpledateformat.format(calander.getTime());

        Log.d("cobaUser", user_id);
        Log.d("cobaUser", location);
        Log.d("cobaUser", now);

        UserMovBody body = new UserMovBody(Integer.parseInt(user_id), location, now);
        mApiService.locationRequest("application/json", body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("responCode", String.valueOf(response.code()));

                        if(response.isSuccessful()) {
                            Log.d("responCode", "Sukses");
                            Toast.makeText(mContext, "Berhasil", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                      }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //mengambar polygon
    private void addGird(final ArrayList outer) {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(outer);
        polygonOptions.strokeColor(Color.rgb(30, 30, 30));
        polygonOptions.fillColor(Color.parseColor("#6627ae60"));
        polygonOptions.strokeWidth(1);
        mMap.addPolygon(polygonOptions);

    }

    public void getGird() {
        mApiService.getGird()
                .enqueue(new Callback<List<GirdBody>>() {
                    @Override
                    public void onResponse(Call<List<GirdBody>> call, Response<List<GirdBody>> response) {
                        Log.d("Doing", "res code :" + response.code() );
                        if (response.isSuccessful()) {
                            List<GirdBody> list = response.body();
                            for (int i =0; i < list.size(); i++){
                                tepi = list.get(i).getGeom().replace("MULTIPOLYGON(((","").replace(")))","").split(",");

                                ArrayList outer = new ArrayList<LatLng>();
                                for (String aTepi : tepi) {
                                    Log.d("poli", aTepi);
                                    String[] latlng = aTepi.split(" ");
                                    batas = new LatLng(Double.parseDouble(latlng[1]), Double.parseDouble(latlng[0]));
                                    outer.add(batas);
                                    Log.d("poli", String.valueOf(batas));
                                }

                                addGird(outer);
                            }
                        }  else {
                            Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<List<GirdBody>> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}