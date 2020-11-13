package com.rohit.rweatherreporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.DashPathEffect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SelectCity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CalendarView calendarView;
    private TextView txtDay,txtMonth,txtTemp,txtWindSpeed,txtPressure,txtHumidity,txtUsername;
    private Button btnSelectDay;
    private Spinner spinner;
    private ProgressBar progressBar;
    private RequestQueue queue;
    LinearLayout LinearCalendar;
    Handler handler;

    double Longitude;
    double Latitude;
    String Date = MainActivity.staticDate+"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        calendarView = findViewById(R.id.calendarView);

        txtUsername = findViewById(R.id.txtUsername);
        progressBar = findViewById(R.id.progressBar);
        txtDay = findViewById(R.id.txtDay);
        txtMonth = findViewById(R.id.txtMonth);
        txtTemp = findViewById(R.id.txtTemp);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);
        txtPressure = findViewById(R.id.txtPressure);
        txtHumidity = findViewById(R.id.txtHumidity);
        btnSelectDay = findViewById(R.id.btnSelectDay);
        spinner = (Spinner) findViewById(R.id.spinner);
        LinearCalendar = findViewById(R.id.LinearCalendar);
        handler = new Handler();

        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Delhi");
        categories.add("Noida");
        categories.add("Mumbai");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        txtUsername.setText(MainActivity.username);

        java.util.Date currentDate = DateTimeUtils.formatDate(MainActivity.staticDate,DateTimeUnits.SECONDS);
        String cd =DateTimeUtils.formatWithStyle(currentDate, DateTimeStyle.SHORT) ;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // i = Year, i1 = month , i2 = date
                String dateformat = i+"-"+(i1+1)+"-"+i2+" 04:00:00";

                java.util.Date date = DateTimeUtils.formatDate(dateformat);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTime(date);
                Date =  (cal.getTimeInMillis() / 1000L)+"";

                Log.e("date",Date+"\n"+dateformat);

                SetCityData(Latitude,Longitude);



                LinearCalendar.animate().alpha(0).setDuration(1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LinearCalendar.setVisibility(View.GONE);
                    }
                },1000);
            }
        });

        btnSelectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearCalendar.setVisibility(View.VISIBLE);
                LinearCalendar.animate().alpha(1).setDuration(1000);
            }
        });

    }

    @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            if(item.equals("Delhi")){
                Longitude = 77.230003;
                Latitude = 28.610001;
                SetCityData(Latitude,Longitude);
            }else if(item.equals("Noida")){
                Longitude = 77.391029;
                Latitude = 	28.535517;
                SetCityData(Latitude,Longitude);
            }else if(item.equals("Mumbai")){
                Longitude = 72.8777;
                Latitude = 19.0760;
                SetCityData(Latitude,Longitude);
            }

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void SetCityData(double Latitude,double Longitude){

        Geocoder geocoder = new Geocoder(SelectCity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
            Address obj = addresses.get(0);

            final String address = obj.getAddressLine(0);
            String Country = obj.getCountryName();
            String AdminArea = obj.getAdminArea(); //State
            String PostalCode = obj.getPostalCode();
            String SubAdminArea = obj.getSubAdminArea();  //District
            String CountryCode = obj.getCountryCode();

//            txtState.setText(AdminArea+" , "+PostalCode);
//            txtCountry.setText(Country);
//            txtDist.setText(SubAdminArea);

            String URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+Latitude+"&lon="+Longitude+"&exclude" +
                    "=hourly&units="+MainActivity.Measurment+"&appid=784e991a7456f917f51e2f5bd86db6a0";

            String url = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat="+Latitude+
                    "&lon="+Longitude+"&units="+MainActivity.Measurment+"&dt="+Date+"&appid=784e991a7456f917f51e2f5bd86db6a0";

            Log.e("url",url);

            queue = Volley.newRequestQueue(SelectCity.this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressBar.setVisibility(View.INVISIBLE);

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


                        java.util.Date date = DateTimeUtils.formatDate(Date, DateTimeUnits.SECONDS);

                        String DateFormat = DateTimeUtils.formatWithStyle(date, DateTimeStyle.FULL)+"";
                        String Day = DateFormat.substring(0,DateFormat.indexOf(","));
                        String Month = DateFormat.replace(Day+", ","");

                        txtDay.setText(Day);
                        txtMonth.setText(Month);
                        txtHumidity.setText(humidity+"%");
                        txtPressure.setText(pressure+"Pa");
                        if(MainActivity.Measurment.equals("metric")){
                            txtTemp.setText(temp+" \u2103");
                            txtWindSpeed.setText(wind_speed+"m/s");
                        }else if(MainActivity.Measurment.equals("imperial")){
                            txtTemp.setText(temp+" \u2109");
                            txtWindSpeed.setText(wind_speed+"m/h");
                        }
//                        JSONArray array=object.getJSONArray("daily");
//                        for(int i =1;i<8;i++){
//                            JSONObject object1=array.getJSONObject(i);
//                            JSONArray weather1 = object1.getJSONArray("weather");
//                            JSONObject wObject1 = (JSONObject) weather1.get(0);
//
//                            JSONObject DayeTempObj = (JSONObject) object1.getJSONObject("temp");
//
//                            int Date1 = Integer.parseInt(object1.getString("dt"));
//                            String temp1 = DayeTempObj.getString("day");
//                            String feels_like1 = object1.getString("feels_like");
//                            String pressure1 = object1.getString("pressure");
//                            String humidity1 = object1.getString("humidity");
//                            String wind_speed1 = object1.getString("wind_speed");
//                            String WeatherDes1 = wObject1.getString("description");
//                            String icon1 = wObject1.getString("icon");
//                            String iconUrl1 = "https://openweathermap.org/img/wn/"+icon1+"@2x.png";
//                            java.util.Date date1 = DateTimeUtils.formatDate(Date1, DateTimeUnits.SECONDS);
//                            String DateFormat1 = DateTimeUtils.formatWithStyle(date1, DateTimeStyle.FULL)+"";
//                            String MonthDate = DateFormat1.replace(", 2020","");
//
//                        }
                    } catch (JSONException e) {
                        Toast.makeText(SelectCity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("error1",e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SelectCity.this, error.toString()+" -requested time is out of allowed range", Toast.LENGTH_LONG).show();
                    Log.e("error",error.toString());
                }
            });
            queue.add(request);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}