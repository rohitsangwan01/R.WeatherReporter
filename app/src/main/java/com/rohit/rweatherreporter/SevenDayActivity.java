package com.rohit.rweatherreporter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SevenDayActivity extends AppCompatActivity {

    TextView txtCurrentDat,txtName,txtCurrentTemp,txtCurrentHumidity,txtCurrentWind,txtAirPressure,txtRealFeel;

    TextView Date1,Date2,Date3,Date4,Date5,Date6,Date7;
    ImageView icon1,icon2,icon3,icon4,icon5,icon6,icon7;
    TextView temp1,temp2,temp3,temp4,temp5,temp6,temp7;
    TextView hum1,hum2,hum3,hum4,hum5,hum6,hum7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ArrayList<Model> weatherData = MainActivity.dataList();

        txtCurrentDat = findViewById(R.id.txtCurrentDat);
        txtName = findViewById(R.id.txtName);
        txtCurrentTemp = findViewById(R.id.txtCurrentTemp);
        txtCurrentHumidity = findViewById(R.id.txtCurrentHumidity);
        txtCurrentWind = findViewById(R.id.txtCurrentWind);
        txtAirPressure = findViewById(R.id.txtPressure);
        txtRealFeel = findViewById(R.id.txtRealFeel);

        Date1 = findViewById(R.id.Date1);
        Date2 = findViewById(R.id.Date2);
        Date3 = findViewById(R.id.Date3);
        Date4 = findViewById(R.id.Date4);
        Date5 = findViewById(R.id.Date5);
        Date6 = findViewById(R.id.Date6);
        Date7 = findViewById(R.id.Date7);

        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);
        icon6 = findViewById(R.id.icon6);
        icon7 = findViewById(R.id.icon7);

        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        temp3 = findViewById(R.id.temp3);
        temp4 = findViewById(R.id.temp4);
        temp5 = findViewById(R.id.temp5);
        temp6 = findViewById(R.id.temp6);
        temp7 = findViewById(R.id.temp7);

        hum1 = findViewById(R.id.hum1);
        hum2 = findViewById(R.id.hum2);
        hum3 = findViewById(R.id.hum3);
        hum4 = findViewById(R.id.hum4);
        hum5 = findViewById(R.id.hum5);
        hum6 = findViewById(R.id.hum6);
        hum7 = findViewById(R.id.hum7);





        txtCurrentDat.setText(weatherData.get(0).getDateFormat());
        txtName.setText(MainActivity.username);
        txtCurrentHumidity.setText(weatherData.get(0).getHumidity()+"%");
        txtAirPressure.setText(weatherData.get(0).getPressure()+"pa");
        if(MainActivity.Measurment.equals("metric")){
            txtCurrentTemp.setText(weatherData.get(0).getTemp()+" \u2103");
            txtCurrentWind.setText(weatherData.get(0).getWind_speed()+"m/s");
            txtRealFeel.setText(weatherData.get(0).getFeels_like()+" \u2103");
        }else if(MainActivity.Measurment.equals("imperial")){
            txtCurrentTemp.setText(weatherData.get(0).getTemp()+" \u2109");
            txtRealFeel.setText(weatherData.get(0).getFeels_like()+" \u2109");
            txtCurrentWind.setText(weatherData.get(0).getWind_speed()+"m/h");
        }

        Date1.setText(weatherData.get(1).getDateFormat());
        hum1.setText(weatherData.get(1).getHumidity()+"%");
        temp1.setText(weatherData.get(1).getTemp()+"°");
        LoadGlide(weatherData.get(1).getIconUrl(),icon1);

        Date2.setText(weatherData.get(2).getDateFormat());
        hum2.setText(weatherData.get(2).getHumidity()+"%");
        temp2.setText(weatherData.get(2).getTemp()+"°");
        LoadGlide(weatherData.get(2).getIconUrl(),icon2);

        Date3.setText(weatherData.get(3).getDateFormat());
        hum3.setText(weatherData.get(3).getHumidity()+"%");
        temp3.setText(weatherData.get(3).getTemp()+"°");
        LoadGlide(weatherData.get(3).getIconUrl(),icon3);

        Date4.setText(weatherData.get(4).getDateFormat());
        hum4.setText(weatherData.get(4).getHumidity()+"%");
        temp4.setText(weatherData.get(4).getTemp()+"°");
        LoadGlide(weatherData.get(4).getIconUrl(),icon4);

        Date5.setText(weatherData.get(5).getDateFormat());
        hum5.setText(weatherData.get(5).getHumidity()+"%");
        temp5.setText(weatherData.get(5).getTemp()+"°");
        LoadGlide(weatherData.get(5).getIconUrl(),icon5);

        Date6.setText(weatherData.get(6).getDateFormat());
        hum6.setText(weatherData.get(6).getHumidity()+"%");
        temp6.setText(weatherData.get(6).getTemp()+"°");
        LoadGlide(weatherData.get(6).getIconUrl(),icon6);

        Date7.setText(weatherData.get(7).getDateFormat());
        hum7.setText(weatherData.get(7).getHumidity()+"%");
        temp7.setText(weatherData.get(7).getTemp()+"°");
        LoadGlide(weatherData.get(7).getIconUrl(),icon7);


    }
    public void LoadGlide(String url,ImageView view){
        Glide.with(SevenDayActivity.this).load(url)
                .override(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop() // this cropping technique scales the image
                .into(view);
    }
}