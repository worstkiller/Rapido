package com.example.rapido.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rapido.R;
import com.example.rapido.controller.LocationServiceProvider;
import com.example.rapido.controller.RoutesProvider;
import com.example.rapido.interfaces.LocationProviderListener;
import com.example.rapido.interfaces.RoutesListener;
import com.example.rapido.util.Util;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LocationProviderListener, RoutesListener {

    public static final String TAG = MainActivity.class.getCanonicalName();
    private static final int REQUEST_CODE = 101;
    @BindView(R.id.atvMainSource)
    AppCompatAutoCompleteTextView atvMainSource;
    @BindView(R.id.atvMainDestination)
    AppCompatAutoCompleteTextView atvMainDestination;
    @BindView(R.id.btSubmit)
    AppCompatButton btSubmit;
    @BindView(R.id.pbMain)
    ProgressBar pbMain;
    private ArrayList<String> mArrayListAddres = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    @SuppressLint("PrivateResource")
    private int dropDownLayoutID = android.support.v7.appcompat.R.layout.select_dialog_item_material;
    private LocationServiceProvider serviceProvider;
    private boolean isSourceActivated = false;
    private RoutesProvider routesProvider;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        listenerSetup();
        checkForLocationPermission();
        initMembers();
    }

    private void initMembers() {
        util = new Util(this);
        if (!util.canGetLocation()) {
            util.showSettingsAlert();
        }
    }

    private void listenerSetup() {
        atvMainDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here close the input method
                hideKeyboard(atvMainDestination);
                atvMainDestination.dismissDropDown();
            }
        });
        atvMainSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here close the input method
                hideKeyboard(atvMainSource);
                atvMainSource.dismissDropDown();
            }
        });

        atvMainDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 2) {
                    getAddress(s.toString());
                    isSourceActivated = false;
                }
            }
        });
        atvMainSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    getAddress(s.toString());
                    isSourceActivated = true;
                }
            }
        });
    }

    private void getAddress(String input) {
        //here make a service call to get the matching addresses
        if (serviceProvider != null) {
            serviceProvider.getLocations(input);
        } else {
            serviceProvider = new LocationServiceProvider(this);
            serviceProvider.getLocations(input);
        }
    }

    private void adapterSetup() {
        //initialize the adapter
        mAdapter = new ArrayAdapter<String>(this, dropDownLayoutID, mArrayListAddres);
        if (isSourceActivated) {
            atvMainSource.setAdapter(mAdapter);
            atvMainSource.showDropDown();
        } else {
            atvMainDestination.setAdapter(mAdapter);
            atvMainDestination.showDropDown();
        }
    }

    @OnClick(R.id.btSubmit)
    public void onViewClicked() {
        //handle the click
        if (isValidated()) {
            checkForDirections();
        } else {
            makeToast(getString(R.string.empty_address));
        }
    }

    private boolean isValidated() {
        if (atvMainDestination.length() > 1 && atvMainSource.length() > 1) {
            return true;
        } else {
            return false;
        }
    }

    private void checkForDirections() {
        pbMain.setVisibility(View.VISIBLE);
        btSubmit.setEnabled(false);
        if (routesProvider != null) {
            routesProvider.getRoutes(atvMainSource.getText().toString(), atvMainDestination.getText().toString());
        } else {
            routesProvider = new RoutesProvider(this);
            routesProvider.getRoutes(atvMainSource.getText().toString(), atvMainDestination.getText().toString());
        }
    }

    @Override
    public void onLocationAvailable(List<String> suggestions) {
        //location is available check for emptiness
        if (suggestions.size() > 0) {
            mArrayListAddres.clear();
            mArrayListAddres.addAll(suggestions);
            adapterSetup();
        } else {
            mArrayListAddres.clear();
            mArrayListAddres.add("No match found!");
            adapterSetup();
        }
        Log.d(TAG, "Size of payload " + suggestions.size());
    }

    @Override
    public void onLocationNotAvailable() {
        //location  is not available, handle the error
        makeToast(getString(R.string.error_msg));
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard(AutoCompleteTextView autoCompleteTextView) {
        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } else {
            //granted, operate normally
        }
    }

    @Override
    public void onRouteAvailable(JsonObject jsonObject) {
        //here get the route and pass it to map activity
        pbMain.setVisibility(View.GONE);
        btSubmit.setEnabled(true);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(TAG, jsonObject.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        clearFields();
    }

    private void clearFields() {
        atvMainSource.setText("");
        atvMainDestination.setText("");
    }

    @Override
    public void onRoutNotAvailable() {
        //here tell the user about error
        pbMain.setVisibility(View.GONE);
        btSubmit.setEnabled(true);
        makeToast(getString(R.string.error_msg));
    }
}
