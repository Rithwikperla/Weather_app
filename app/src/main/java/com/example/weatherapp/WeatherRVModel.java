package com.example.weatherapp;

public class WeatherRVModel {

    String time;
    String temp;
    String icon;
    String windspeed;

    public WeatherRVModel(String time, String temp, String icon, String windspeed) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.windspeed = windspeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
}
