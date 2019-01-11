package com.sky.happyf.Model;

import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {
    public String id;
    public String name;
    public String cover;
    public List<SmallType> smallTypeList;

    public Type() {
    }


}
