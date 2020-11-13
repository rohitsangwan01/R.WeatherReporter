package com.rohit.rweatherreporter;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences pref;

    static String lat;

    LinearLayout LinearGetName;
    EditText edtUsername;
    Button btnDone;
    SharedPreferences.Editor editor;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    Address obj;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        LinearGetName = findViewById(R.id.LinearGetName);
        LinearGetName.setVisibility(View.INVISIBLE);

        edtUsername = findViewById(R.id.edtUsername);
        btnDone = findViewById(R.id.btnDone);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        String username = pref.getString("username","");


        if (username.equals("")) {
            LinearGetName.setVisibility(View.VISIBLE);
            LinearGetName.animate().alpha(1).setDuration(1000);
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edtUsername.getText().toString().equals("")){
                        edtUsername.setError("Enter username");
                    }else{

                        editor.putString("username", edtUsername.getText().toString());
                        editor.apply();
                        getPermission();
                    }
                }
            });
        }else{
            getPermission();
        }

    }

    //To Get Permission
    public void getPermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                      //  Toast.makeText(SplashScreen.this, "Granted", Toast.LENGTH_SHORT).show();
                        GoNext();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(SplashScreen.this, "You Have To Grant Permission To Use This App", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    //Go To Main Activity
    public void GoNext(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
            }
        },1000);
    }


}