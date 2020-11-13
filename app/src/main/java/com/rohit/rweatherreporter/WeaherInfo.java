package com.rohit.rweatherreporter;

public class WeaherInfo {
    String temp,wind,weather,icon;

    public WeaherInfo(String temp, String wind, String weather, String icon) {
        this.temp = temp;
        this.wind = wind;
        this.weather = weather;
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
