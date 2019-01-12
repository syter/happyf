package com.sky.happyf.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    public String id;
    public String cover;
    public String goodsId;
    public String title;
    public String selectedType;
    public String price;
    public String shellPrice;
    public String count;
    public String date;
    public boolean isInvalid;
    public int type;
    public boolean isSeaFood;
    public boolean isFreePost;
    public boolean selected;
    public boolean removeSelected;

    public Cart() {
    }


}
