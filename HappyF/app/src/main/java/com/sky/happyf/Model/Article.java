package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    public String id;
    public String cover;
    public String covers;
    public String title;
    public String desc;
    public List<String> contents;
    public String date;
    public String authorName;
    public boolean isTop;
    public int readCount;
    public int type;
    public String categoryId;
    public String contentUrl;

    public Article() {
    }


}
