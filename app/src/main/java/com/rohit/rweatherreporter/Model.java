package com.rohit.rweatherreporter;

public class Model {

    String temp;
    String feels_like;
    String pressure;
    String humidity;
    String wind_speed;
    String WeatherDes;
    String iconUrl;
    String DateFormat;

    public Model(String temp, String feels_like, String pressure, String humidity, String wind_speed, String weatherDes, String iconUrl, String dateFormat) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        WeatherDes = weatherDes;
        this.iconUrl = iconUrl;
        DateFormat = dateFormat;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getWeatherDes() {
        return WeatherDes;
    }

    public void setWeatherDes(String weatherDes) {
        WeatherDes = weatherDes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDateFormat() {
        return DateFormat;
    }

    public void setDateFormat(String dateFormat) {
        DateFormat = dateFormat;
    }
}
