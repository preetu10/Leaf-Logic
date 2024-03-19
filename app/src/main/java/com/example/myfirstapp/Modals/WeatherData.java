package com.example.myfirstapp.Modals;

import java.util.List;

public class WeatherData {
    private List<weather> weather;

    private main main;
    private String name;

    public WeatherData(List<com.example.myfirstapp.Modals.weather> weather, com.example.myfirstapp.Modals.main main, String name) {
        this.weather = weather;
        this.main = main;
        this.name = name;
    }

    public List<com.example.myfirstapp.Modals.weather> getWeather() {
        return weather;
    }

    public void setWeather(List<com.example.myfirstapp.Modals.weather> weather) {
        this.weather = weather;
    }

    public com.example.myfirstapp.Modals.main getMain() {
        return main;
    }

    public void setMain(com.example.myfirstapp.Modals.main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
