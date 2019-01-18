package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Happy implements Serializable {
    public String id;
    public String title1;
    public String title2;
    public String title3;
    public String longitude;
    public String latitude;
    public List<String> covers;
    public String price;
    public List<String> descCovers;
    public List<SelectType> selectTypeList;
    public String content;

    public Happy() {
    }


}
