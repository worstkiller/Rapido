package com.example.rapido.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

import com.example.rapido.R;
import com.example.rapido.fragment.DialogFragmentRoutes;
import com.example.rapido.util.Route;
import com.example.rapido.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_GPS = 102;
    public static final String TAG = MapsActivity.class.getCanonicalName();
    @BindView(R.id.btSubmit)
    AppCompatButton btSubmit;
    private GoogleMap mMap;
    private static final int REQUEST_CODE = 101;
    private Util mUtil;
    private String jsonObject;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        initializeMembers();
        checkGPS();
    }

    private void checkGPS() {
        if (mUtil.canGetLocation()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            mUtil.showSettingsAlert();
        }
    }

    private void initializeMembers() {
        mUtil = new Util(this);
        jsonObject = getIntent().getStringExtra(MainActivity.TAG);
        route = new Route();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mUtil.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                LatLng current = new LatLng(mUtil.getLatitude(), mUtil.getLongitude());
                mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                drawRoute();
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        } else {
            makeToast(getString(R.string.gps_message));
        }
    }

    private void drawRoute() {
        route.drawRoute(mMap, this, jsonObject);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            //here we have permissions
        } else {
            makeToast(getString(R.string.access_denied));
            finish();
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btSubmit)
    public void onViewClicked() {
        //here open the dialog
        Bundle bundle = new Bundle();
        bundle.putString(MapsActivity.TAG, jsonObject);
        DialogFragmentRoutes dialogFragmentRoutes = new DialogFragmentRoutes();
        dialogFragmentRoutes.setArguments(bundle);
        dialogFragmentRoutes.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.MyMaterialTheme);
        dialogFragmentRoutes.show(getSupportFragmentManager(), TAG);
    }
}
