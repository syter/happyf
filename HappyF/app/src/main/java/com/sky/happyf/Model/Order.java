package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    public String id;
    public String orderNo;
    public String postagePrice;
    public String totalPrice;
    public String totalShellPrice;
    public String postPrice;
    public String postShellPrice;
    public String payType;
    public String postNo;
    public String postName;
    public List<Cart> cartList;
    public String status;
    public String leftTime;

    public Order() {
    }


}
