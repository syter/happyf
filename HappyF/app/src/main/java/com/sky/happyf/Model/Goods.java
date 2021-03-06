package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {
    public String id;
    public String cover;
    public List<String> covers;
    public String title1;
    public String title2;
    public String title3;
    public String desc;
    public String content;
    public List<SelectType> selectTypes;
    public String price;
    public String shellPrice;
    public String backPrice;
    public String sellCount;
    public boolean hasMorePrice;
    public int state;
    public boolean isSeaFood;
    public String unit;
    public String postageRule;
    public String serviceType;
    public List<String> descCovers;

    public Goods() {
    }


}
