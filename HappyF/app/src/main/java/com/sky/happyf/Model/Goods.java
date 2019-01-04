package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {
    public String id;
    public String covers;
    public String title;
    public String desc;
    public String content;
    public List<String> sizes;
    public String price;
    public String shellPrice;
    public String backPrice;
    public int sell_count;
    public int state;

    public Goods() {
    }


}
