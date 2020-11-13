package com.rohit.rweatherreporter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.UserInfo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {


    static String username;
    static double longitude,latitiude;

    static String Measurment = "metric";

    static ArrayList<Model> weatherData;

    LinearLayout LinearSetting;

    TextView txtDay,txtMonth,txtTemp,txtRealFeel,txtWindSpeed,txtPressure,txtHumidity,txtWeather,txtUsernameSetting;
    ImageView imgWeather;

    Button btnSetCelcius,btnSetFren;
    //standard, metric,imperial
    ImageView btnSevenDay,btnSetting,btnSelectCity,buttonCurrentTab;

    //Temperature. Units – default: kelvin, metric: Celsius, imperial Fahrenheit.
    //Atmospheric pressure on the sea level, hPa
    //Wind speed. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour.

    TextView txtUsername,txtDist,txtCountry,txtState;

    ProgressBar _progressBar;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Address obj;
    static int staticDate;


    RequestQueue queue;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        txtUsername = findViewById(R.id.txtUsername);
        txtDist = findViewById(R.id.txtDist);
        _progressBar = findViewById(R.id.progressBar);
        txtCountry = findViewById(R.id.txtCountry);
        txtState = findViewById(R.id.txtState);
        txtDay = findViewById(R.id.txtDay);
        txtMonth = findViewById(R.id.txtMonth);
        txtTemp = findViewById(R.id.txtTemp);
        txtRealFeel = findViewById(R.id.txtRealFeel);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);
        txtPressure = findViewById(R.id.txtPressure);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWeather = findViewById(R.id.txtWeather);
        imgWeather = findViewById(R.id.imgWeather);
        btnSevenDay = findViewById(R.id.btnSevenDay);
        LinearSetting = findViewById(R.id.LinearSetting);
        btnSetting = findViewById(R.id.btnSetting);
        txtUsernameSetting = findViewById(R.id.txtUsernameSetting);
        btnSetCelcius = findViewById(R.id.btnSetCelcius);
        btnSetFren =  findViewById(R.id.btnSetFern);
        btnSelectCity = findViewById(R.id.btnSelectCity);
        buttonCurrentTab = findViewById(R.id.buttonCurrentTab);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else{
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        }
        mLocationRequest = new LocationRequest();

        weatherData = new ArrayList<>();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        username=pref.getString("username", "default");
        txtUsername.setText(username);

        btnSevenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weatherData.size() == 8){
                    Intent intent = new Intent(MainActivity.this,SevenDayActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Data Loading, Please Wait...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weatherData.size() == 8){
                    txtUsernameSetting.setText("Hey "+username);
                    if(LinearSetting.getVisibility() == View.VISIBLE){
                        LinearSetting.animate().alpha(0).setDuration(1000);
                        LinearSetting.setVisibility(View.GONE);
                    }else{
                        LinearSetting.setVisibility(View.VISIBLE);
                        LinearSetting.animate().alpha(1).setDuration(1000);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Data Loading, Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSetFren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LinearSetting.getVisibility() == View.VISIBLE){
                    Measurment = "imperial";
                    _progressBar.setVisibility(View.VISIBLE);
                    LinearSetting.animate().alpha(0).setDuration(1000);
                    LinearSetting.setVisibility(View.GONE);
                    SetData(latitiude,longitude);

                }
            }
        });
        btnSetCelcius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LinearSetting.getVisibility() == View.VISIBLE){
                    Measurment = "metric";
                    _progressBar.setVisibility(View.VISIBLE);
                    LinearSetting.animate().alpha(0).setDuration(1000);
                    LinearSetting.setVisibility(View.GONE);
                    SetData(latitiude,longitude);
                }
            }
        });

        btnSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(weatherData.size() == 8){
                    Intent intent = new Intent(MainActivity.this,SelectCity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Data Loading, Please Wait...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonCurrentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You Are Already On Current Tab", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Location services connection failed with code " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        }
    }

    public void settingRequest() {
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "App Can't Work Properly without Location", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                double Latitude = mLastLocation.getLatitude();
                double Longitude = mLastLocation.getLongitude();
                latitiude = Latitude;
                longitude = Longitude;
                SetData(Latitude,Longitude);
            } else {
                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MainActivity.this);
            }
        }
    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        double Latitude = mLastLocation.getLatitude();
        double Longitude = mLastLocation.getLongitude();
        SetData(Latitude,Longitude);
    }

    public void SetData(double Latitude,double Longitude) {
        weatherData.clear();
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
            obj = addresses.get(0);

            final String address = obj.getAddressLine(0);
            String Country = obj.getCountryName();
            String AdminArea = obj.getAdminArea(); //State
            String PostalCode = obj.getPostalCode();
            String SubAdminArea = obj.getSubAdminArea();  //District
            String CountryCode = obj.getCountryCode();

            txtState.setText(AdminArea+" , "+PostalCode);
            txtCountry.setText(Country);
            txtDist.setText(SubAdminArea);

            String URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+Latitude+"&lon="+Longitude+"&exclude" +
                    "=hourly&units="+Measurment+"&appid=784e991a7456f917f51e2f5bd86db6a0";

            queue = Volley.newRequestQueue(MainActivity.this);
            StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        _progressBar.setVisibility(View.INVISIBLE);

                        JSONObject object=new JSONObject(response);



                        JSONObject current = (JSONObject) object.get("current");
                        JSONArray weather = current.getJSONArray("weather");
                        JSONObject wObject = (JSONObject) weather.get(0);

                        int Date = Integer.parseInt(current.getString("dt"));
                        String temp = current.getString("temp");
                        String feels_like = current.getString("feels_like");
                        String pressure = current.getString("pressure");
                        String humidity = current.getString("humidity");
                        String wind_speed = current.getString("wind_speed");
                        String WeatherDes = wObject.getString("description");
                        String icon = wObject.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/"+icon+"@2x.png";

                        staticDate = Date;


                        Glide.with(MainActivity.this).load(iconUrl)
                                .override(200, 200) // resizes the image to these dimensions (in pixel)
                                .centerCrop() // this cropping technique scales the image
                                .into(imgWeather);


                        java.util.Date date = DateTimeUtils.formatDate(Date, DateTimeUnits.SECONDS);

                        String DateFormat = DateTimeUtils.formatWithStyle(date, DateTimeStyle.FULL)+"";
                        String Day = DateFormat.substring(0,DateFormat.indexOf(","));
                        String Month = DateFormat.replace(Day+", ","");

                        weatherData.add(new Model(temp,feels_like,pressure,humidity,wind_speed,WeatherDes,iconUrl,DateFormat));

                        txtDay.setText(Day);
                        txtMonth.setText(Month);

                        txtHumidity.setText(humidity+"%");
                        txtPressure.setText(pressure+"Pa");
                        txtWeather.setText(WeatherDes);

                        if(Measurment.equals("metric")){
                            txtTemp.setText(temp+" \u2103");
                            txtRealFeel.setText(feels_like+"\u2103");
                            txtWindSpeed.setText(wind_speed+"m/s");
                        }else if(Measurment.equals("imperial")){
                            txtTemp.setText(temp+" \u2109");
                            txtRealFeel.setText(feels_like+"\u2109");
                            txtWindSpeed.setText(wind_speed+"m/h");
                        }


                        JSONArray array=object.getJSONArray("daily");
                        for(int i =1;i<8;i++){
                            JSONObject object1=array.getJSONObject(i);
                            JSONArray weather1 = object1.getJSONArray("weather");
                            JSONObject wObject1 = (JSONObject) weather1.get(0);

                            JSONObject DayeTempObj = (JSONObject) object1.getJSONObject("temp");

                            int Date1 = Integer.parseInt(object1.getString("dt"));
                            String temp1 = DayeTempObj.getString("day");
                            String feels_like1 = object1.getString("feels_like");
                            String pressure1 = object1.getString("pressure");
                            String humidity1 = object1.getString("humidity");
                            String wind_speed1 = object1.getString("wind_speed");
                            String WeatherDes1 = wObject1.getString("description");
                            String icon1 = wObject1.getString("icon");
                            String iconUrl1 = "https://openweathermap.org/img/wn/"+icon1+"@2x.png";
                            java.util.Date date1 = DateTimeUtils.formatDate(Date1, DateTimeUnits.SECONDS);
                            String DateFormat1 = DateTimeUtils.formatWithStyle(date1, DateTimeStyle.FULL)+"";
                            String MonthDate = DateFormat1.replace(", 2020","");

                            weatherData.add(new Model(temp1,feels_like1,pressure1,humidity1,wind_speed1,WeatherDes1,iconUrl1,MonthDate));


                        }





                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("error1",e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    _progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("error",error.toString());
                    txtDist.setText(error.toString());
                }
            });
            queue.add(request);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<Model> dataList(){
        return weatherData;
    }
}