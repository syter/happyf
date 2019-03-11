package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {
    public String id;
    public String date;
    public String desc;
    public String weatherType;
    public String lowWave;
    public String lowWaveTime;
    public String highWaveTime;
    public String highWave;
    public String lowTemp;
    public String highTemp;
    public List<String> waveList;
    public String currentTemp;
    public String windDirection;
    public String wind;

    public Weather() {
    }


}
