package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Rank implements Serializable {
    public String id;
    public String fishName;
    public String date;
    public String cover;
    public String reward;
    public List<RankData> rankDataList;

    public Rank() {
    }


}
