package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    public String id;
    public String covers;
    public String title;
    public String desc;
    public String content;
    public String date;
    public int read_count;
    public int type;

    public Article() {
    }


}
